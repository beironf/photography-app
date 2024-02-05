package backend.photography.api

import backend.common.api.model.ApiHttpErrors.*
import backend.common.api.model.ApiHttpResponse.*
import backend.common.api.utils.ApiServiceSupport
import ApiSpecs.ImageFileUpload
import backend.photography.api.model.*
import backend.photography.api.model.enums.CategoryDto
import backend.photography.entities.image.ImageIO
import backend.photography.interactors.{ImageExifService, ImageService, PhotoService, Validator}
import backend.photography.ports.{ImageExifRepository, ImageRepository, PhotoRepository}
import sttp.capabilities.akka.AkkaStreams

import java.io.File
import java.time.Instant
import scala.concurrent.{ExecutionContext, Future}

object ApiService {
  def apply(photoRepository: PhotoRepository,
            imageRepository: ImageRepository,
            exifRepository: ImageExifRepository)
           (implicit executionContext: ExecutionContext): ApiService = {
    val validator = new Validator(photoRepository, imageRepository)
    val photoService = PhotoService(validator, photoRepository)
    val imageService = ImageService(validator, imageRepository)
    val exifService = ImageExifService(validator, exifRepository)
    new ApiService(photoService, imageService, exifService)
  }
}

class ApiService(photoService: PhotoService,
                 imageService: ImageService,
                 exifService: ImageExifService)
                (implicit executionContext: ExecutionContext) extends ApiServiceSupport
  with ApiExceptionHandler
  with ImageIO
  with ImplicitDtoConversion {

  private val MAX_IMAGE_SIZE = 4000
  private val MAX_THUMBNAIL_SIZE = 1200

  def getPhoto(imageId: String): Future[EnvelopedHttpResponse[PhotoDto]] =
    photoService.getPhoto(imageId)
      .map(_.map(_.toDto))
      .toEnvelopedHttpResponse(specificExceptionHandling)

  def listPhotos(category: Option[CategoryDto] = None,
                 group: Option[String] = None,
                 rating: Option[Int] = None,
                 inShowroom: Option[Boolean] = None): Future[EnvelopedHttpResponse[Seq[PhotoWithRatioDto]]] = (for {
    cat <- Future.apply(category.map(_.toDomain))
      .recover { case _: NoSuchElementException => throw BadRequestException(s"'${category.get}' is not a valid category") }
    photos <- photoService.listPhotos(cat, group, rating, inShowroom)
    exifList <- exifService.listExif(Some(photos.map(_.imageId)))
    width = exifList.map { case (imageId, exif) => imageId -> exif.width }.toMap
    height = exifList.map { case (imageId, exif) => imageId -> exif.height }.toMap
  } yield photos.sortBy(_.taken).reverse.map { photo =>
    photo.toDtoWithRatio(width.getOrElse(photo.imageId, 1), height.getOrElse(photo.imageId, 1))
  }).toEnvelopedHttpResponse

  def listPhotoGroups: Future[EnvelopedHttpResponse[Seq[String]]] =
    photoService.listPhotoGroups
      .toEnvelopedHttpResponse

  def addPhoto(photoDto: PhotoDto): Future[HttpResponse[Unit]] = (for {
    _ <- validator.photoDoesNotExist(photoDto.imageId).toEitherT
    _ <- photoService.addPhoto(photoDto.toDomain).toEitherT[HttpError]
  } yield (): Unit).value

  def updatePhoto(imageId: String, updateDto: UpdatePhotoDto): Future[HttpResponse[Unit]] = (for {
    _ <- validator.photoExists(imageId).toEitherT
    _ <- photoService.updatePhoto(imageId, updateDto.toDomain).toEitherT[HttpError]
  } yield (): Unit).value

  def removePhoto(imageId: String): Future[HttpResponse[Unit]] = (for {
    _ <- validator.photoExists(imageId).toEitherT
    _ <- photoService.removePhoto(imageId).toEitherT[HttpError]
  } yield (): Unit).value

  def getImage(imageId: String): Future[HttpResponse[AkkaStreams.BinaryStream]] =
    imageService.getImageStream(imageId).map {
      case Some(stream) => Right(stream)
      case None => Left(NotFound(s"[imageId: $imageId] Image not found"))
    }

  def getThumbnail(imageId: String): Future[HttpResponse[AkkaStreams.BinaryStream]] =
    imageService.getThumbnailStream(imageId).map {
      case Some(stream) => Right(stream)
      case None => Left(NotFound(s"[imageId: $imageId] Thumbnail not found"))
    }

  def getSiteImage(fileName: String): Future[HttpResponse[AkkaStreams.BinaryStream]] =
    imageService.getSiteImageStream(fileName).map {
      case Some(stream) => Right(stream)
      case None => Left(NotFound(s"[fileName: $fileName] Site image not found"))
    }

  def listImages: Future[EnvelopedHttpResponse[Seq[ImageDto]]] =
    (for {
      ids <- imageService.listImageIds
      exifList <- exifService.listExif(Some(ids))
      widthById = exifList.map { case (imageId, exif) => imageId -> exif.width }.toMap
      heightById = exifList.map { case (imageId, exif) => imageId -> exif.height }.toMap
      dateById = exifList.map { case (imageId, exif) => imageId -> exif.date.getOrElse(Instant.now) }.toMap
    } yield ids
      .sortWith { case (a, b) => dateById(a).isAfter(dateById(b)) }
      .map(id => ImageDto(id, widthById.getOrElse(id, 1), heightById.getOrElse(id, 1))))
      .toEnvelopedHttpResponse

  def getImageExif(imageId: String): Future[EnvelopedHttpResponse[ImageExifDto]] =
    exifService.getExif(imageId).map {
      case Some(exif) => Right(exif.toDto).asInstanceOf[Right[HttpError, ImageExifDto]]
      case None => Left(NotFound(s"[imageId: $imageId] Metadata not found"))
    }.toEnveloped

  def uploadImage(form: ImageFileUpload): Future[HttpResponse[Unit]] =
    (for {
      fileName <- validator.fileHasFileName(form.image).toEitherT[Future]
      _ <- validator.fileIsJPG(fileName).toEitherT[Future]
      _ <- validator.imageDoesNotExist(fileName).toEitherT
      exif = ExifUtil.getExif(form.image.body)
      _ <- exifService.addExif(fileName, exif).toEitherT[HttpError]
      _ <- resizeAndUploadImage(fileName, form.image.body).toEitherT[HttpError]
      _ <- resizeAndUploadImage(fileName, form.image.body, MAX_THUMBNAIL_SIZE, imageService.uploadThumbnail).toEitherT[HttpError]
    } yield (): Unit).value

  private def resizeAndUploadImage(fileName: String,
                                   file: File,
                                   maxSize: Int = MAX_IMAGE_SIZE,
                                   upload: (String, Array[Byte]) => Future[Unit] = imageService.uploadImage): Future[Unit] = {
    val resizedImage = ImageResizer.resizeImage(file, maxSize)
    upload(fileName, resizedImage)
  }

  def removeImage(imageId: String): Future[HttpResponse[Unit]] = {
    (for {
      _ <- validator.imageExists(imageId).toEitherT
      _ <- imageService.removeThumbnail(imageId).toEitherT[HttpError]
      _ <- exifService.removeExif(imageId).toEitherT[HttpError]
      _ <- imageService.removeImage(imageId).toEitherT[HttpError]
    } yield (): Unit).value
  }

}
