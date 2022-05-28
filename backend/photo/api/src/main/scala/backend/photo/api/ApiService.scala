package backend.photo.api

import backend.common.api.model.ApiHttpErrors.HttpError
import backend.common.api.model.ApiHttpResponse._
import backend.common.api.utils.ApiServiceSupport
import backend.photo.api.model.ImplicitDtoConversion
import backend.photo.api.model.dtos.PhotoDto
import backend.photo.interactors.PhotoService

import scala.concurrent.{ExecutionContext, Future}

class ApiService(service: PhotoService, validator: ApiValidationService)
                (implicit executionContext: ExecutionContext) extends ApiServiceSupport
  with ImplicitDtoConversion {

  def getPhoto(imageId: String): Future[EnvelopedHttpResponse[PhotoDto]] =
    validator.photoExists(imageId)
      .map(_.map(_.toDto))
      .toEnveloped

  def listPhotos(group: Option[String] = None,
                 rating: Option[Int] = None): Future[EnvelopedHttpResponse[Seq[PhotoDto]]] =
    service.listPhotos(group, rating)
      .map(_.map(_.toDto))
      .toEnvelopedHttpResponse

  def addPhoto(photoDto: PhotoDto): Future[HttpResponse[Unit]] = (for {
    _ <- validator.photoDoesNotExist(photoDto.imageId).toEitherT
    _ <- service.addPhoto(photoDto.toDomain).toEitherT[HttpError]
  } yield (): Unit).value

  def removePhoto(imageId: String): Future[HttpResponse[Unit]] = (for {
    _ <- validator.photoExists(imageId).toEitherT
    _ <- service.removePhoto(imageId).toEitherT[HttpError]
  } yield (): Unit).value

}
