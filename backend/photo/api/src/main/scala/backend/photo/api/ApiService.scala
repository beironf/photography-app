package backend.photo.api

import backend.common.api.model.ApiHttpErrors.HttpError
import backend.common.api.model.ApiHttpResponse._
import backend.common.api.utils.ApiServiceSupport
import backend.exif.interactors.ImageExifService
import backend.photo.api.model.ImplicitDtoConversion
import backend.photo.api.model.dtos._
import backend.photo.interactors.PhotoService

import scala.concurrent.{ExecutionContext, Future}

class ApiService(service: PhotoService, validator: ApiValidationService, exifService: ImageExifService)
                (implicit executionContext: ExecutionContext) extends ApiServiceSupport
  with ImplicitDtoConversion {

  def getPhoto(imageId: String): Future[EnvelopedHttpResponse[PhotoDto]] =
    validator.photoExists(imageId)
      .map(_.map(_.toDto))
      .toEnveloped

  def listPhotos(group: Option[String] = None,
                 rating: Option[Int] = None): Future[EnvelopedHttpResponse[Seq[PhotoWithRatioDto]]] = (for {
    photos <- service.listPhotos(group, rating)
    exifList <- exifService.listExif(Some(photos.map(_.imageId)))
    width = exifList.map { case (imageId, exif) => imageId -> exif.width }.toMap
    height = exifList.map { case (imageId, exif) => imageId -> exif.height }.toMap
  } yield photos.sortBy(_.taken).reverse.map { photo =>
    photo.toDtoWithRatio(width.getOrElse(photo.imageId, 1), height.getOrElse(photo.imageId, 1))
  }).toEnvelopedHttpResponse

  def addPhoto(photoDto: PhotoDto): Future[HttpResponse[Unit]] = (for {
    _ <- validator.photoDoesNotExist(photoDto.imageId).toEitherT
    _ <- service.addPhoto(photoDto.toDomain).toEitherT[HttpError]
  } yield (): Unit).value

  def updatePhoto(imageId: String, updateDto: UpdatePhotoDto): Future[HttpResponse[Unit]] = (for {
    _ <- validator.photoExists(imageId).toEitherT
    _ <- service.updatePhoto(imageId, updateDto.toDomain).toEitherT[HttpError]
  } yield (): Unit).value

  def removePhoto(imageId: String): Future[HttpResponse[Unit]] = (for {
    _ <- validator.photoExists(imageId).toEitherT
    _ <- service.removePhoto(imageId).toEitherT[HttpError]
  } yield (): Unit).value

}
