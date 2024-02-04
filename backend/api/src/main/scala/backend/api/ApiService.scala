package backend.api

import backend.common.api.model.ApiHttpErrors.*
import backend.common.api.model.ApiHttpResponse.*
import backend.common.api.utils.ApiServiceSupport
import ApiSpecs.ImageFileUpload
import backend.api.model.*
import backend.api.model.enums.*
import backend.common.model.CommonExceptions.BadRequestException
import backend.image.entities.ImageIO
import backend.image.interactors.ImageService
import backend.exif.interactors.ImageExifService
import backend.exif.ports.ImageExifRepository
import backend.image.ports.ImageRepository
import backend.photo.interactors.PhotoService
import backend.photo.ports.PhotoRepository
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
    val photoService = new PhotoService(photoRepository)
    val imageService = new ImageService(imageRepository)
    val exifService = new ImageExifService(exifRepository)
    new ApiService(photoService, imageService, exifService, validator)
  }
}

class ApiService(photoService: PhotoService,
                 imageService: ImageService,
                 exifService: ImageExifService,
                 validator: Validator)
                (implicit executionContext: ExecutionContext) extends ApiServiceSupport
  with ImageIO
  with ImplicitDtoConversion {

  private val MAX_IMAGE_SIZE = 4000
  private val MAX_THUMBNAIL_SIZE = 1200

  def getPhoto(imageId: String): Future[EnvelopedHttpResponse[PhotoDto]] =
    validator.photoExists(imageId)
      .map(_.map(_.toDto))
      .toEnveloped

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
