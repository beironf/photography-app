package backend.photo.api

import backend.common.api.model.ApiHttpResponse._
import backend.common.api.utils.ApiExceptionsHandler
import backend.common.model.CommonExceptions._
import backend.photo.api.model.ImplicitDomainConversion
import backend.photo.api.model.request.AddPhoto
import backend.photo.api.model.response._
import backend.photo.entities.meta._
import backend.photo.interactors.PhotoService

import scala.concurrent.{ExecutionContext, Future}

class ApiService(service: PhotoService, validator: ApiValidationService)
                (implicit executionContext: ExecutionContext) extends ApiExceptionsHandler
  with ImplicitDomainConversion {

  @throws[NotFoundException]
  def getPhoto(imageId: String): Future[EnvelopedHttpResponse[PhotoDto]] =
    validator.photoExists(imageId)
      .map(_.toDto)
      .toEnvelopedHttpResponse

  def listPhotos(category: Option[Category.Value] = None,
                 rating: Option[Int] = None): Future[EnvelopedHttpResponse[Seq[PhotoDto]]] =
    service.listPhotos(category, rating)
      .map(_.map(_.toDto))
      .toEnvelopedHttpResponse

  @throws[BadRequestException]
  def addPhoto(addPhoto: AddPhoto): Future[HttpResponse[Unit]] = (for {
    _ <- validator.photoDoesNotExist(addPhoto.imageId)
    _ <- service.addPhoto(addPhoto.toDomain)
  } yield (): Unit)
    .toHttpResponse

  def removePhoto(imageId: String): Future[HttpResponse[Unit]] = (for {
    _ <- validator.photoExists(imageId)
    _ <- service.removePhoto(imageId)
  } yield (): Unit)
    .toHttpResponse

}
