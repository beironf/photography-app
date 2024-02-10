package backend.photography.api

import backend.common.api.model.ApiHttpResponse.*
import backend.common.api.utils.ApiServiceSupport
import ApiSpecs.ImageFileUpload
import backend.photography.api.model.*
import backend.photography.api.model.enums.CategoryDto
import backend.photography.entities.image.ImageIO
import backend.photography.interactors.validation.Validator
import backend.photography.interactors.*
import backend.photography.ports.interactors.*
import backend.photography.ports.repositories.*
import sttp.capabilities.akka.AkkaStreams

import java.time.Instant
import scala.concurrent.{ExecutionContext, Future}

object ApiService {
  def apply(photoRepository: PhotoRepository,
            imageRepository: ImageRepository,
            exifRepository: ImageExifRepository)
           (implicit executionContext: ExecutionContext): ApiService = {
    val validator = new Validator(photoRepository, imageRepository, exifRepository)
    val photoService = PhotoServiceImpl(validator, photoRepository)
    val imageService = ImageServiceImpl(validator, imageRepository, exifRepository)
    val exifService = ImageExifServiceImpl(validator, exifRepository)
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

  def getPhoto(imageId: String): Future[EnvelopedHttpResponse[PhotoDto]] =
    photoService.getPhoto(imageId)
      .map(_.map(_.toDto))
      .toEnvelopedHttpResponse(specificExceptionHandling)

  def listPhotos(category: Option[CategoryDto] = None,
                 group: Option[String] = None,
                 rating: Option[Int] = None,
                 inShowroom: Option[Boolean] = None): Future[EnvelopedHttpResponse[Seq[PhotoWithRatioDto]]] = (for {
    photos <- photoService.listPhotos(category.map(_.toDomain), group, rating, inShowroom).toEitherT
    exifList <- exifService.listExif(Some(photos.map(_.imageId))).toEitherT
    width = exifList.map { case (imageId, exif) => imageId -> exif.width }.toMap
    height = exifList.map { case (imageId, exif) => imageId -> exif.height }.toMap
  } yield photos.map { photo =>
    photo.toDtoWithRatio(width.getOrElse(photo.imageId, 1), height.getOrElse(photo.imageId, 1))
  }).value
    .toEnvelopedHttpResponse(specificExceptionHandling)

  def listPhotoGroups: Future[EnvelopedHttpResponse[Seq[String]]] =
    photoService.listPhotoGroups
      .toEnvelopedHttpResponse(specificExceptionHandling)

  def addPhoto(photoDto: PhotoDto): Future[HttpResponse[Unit]] =
    photoService.addPhoto(photoDto.toDomain)
      .toHttpResponse(specificExceptionHandling)

  def updatePhoto(imageId: String, updateDto: UpdatePhotoDto): Future[HttpResponse[Unit]] =
    photoService.updatePhoto(imageId, updateDto.toDomain)
      .toHttpResponse(specificExceptionHandling)

  def removePhoto(imageId: String): Future[HttpResponse[Unit]] =
    photoService.removePhoto(imageId)
      .toHttpResponse(specificExceptionHandling)

  def getImage(imageId: String): Future[HttpResponse[AkkaStreams.BinaryStream]] =
    imageService.getImageStream(imageId)
      .toHttpResponse(specificExceptionHandling)

  def getThumbnail(imageId: String): Future[HttpResponse[AkkaStreams.BinaryStream]] =
    imageService.getThumbnailStream(imageId)
      .toHttpResponse(specificExceptionHandling)

  def getSiteImage(fileName: String): Future[HttpResponse[AkkaStreams.BinaryStream]] =
    imageService.getSiteImageStream(fileName)
      .toHttpResponse(specificExceptionHandling)

  def listImages: Future[EnvelopedHttpResponse[Seq[ImageDto]]] = (for {
    ids <- imageService.listImageIds.toEitherT
    exifList <- exifService.listExif(Some(ids)).toEitherT
    widthById = exifList.map { case (imageId, exif) => imageId -> exif.width }.toMap
    heightById = exifList.map { case (imageId, exif) => imageId -> exif.height }.toMap
    dateById = exifList.map { case (imageId, exif) => imageId -> exif.date.getOrElse(Instant.now) }.toMap
  } yield { ids
    .sortWith { case (a, b) => dateById(a).isAfter(dateById(b)) }
    .map { id => ImageDto(id, widthById.getOrElse(id, 1), heightById.getOrElse(id, 1)) }
  }).value
    .toEnvelopedHttpResponse(specificExceptionHandling)

  def getImageExif(imageId: String): Future[EnvelopedHttpResponse[ImageExifDto]] =
    exifService.getExif(imageId)
      .map(_.map(_.toDto))
      .toEnvelopedHttpResponse(specificExceptionHandling)

  def uploadImage(form: ImageFileUpload): Future[HttpResponse[Unit]] =
    imageService.uploadImage(form.image.fileName, form.image.body)
      .toHttpResponse(specificExceptionHandling)

  def removeImage(imageId: String): Future[HttpResponse[Unit]] =
    imageService.removeImage(imageId).toEitherT.value
      .toHttpResponse(specificExceptionHandling)

}
