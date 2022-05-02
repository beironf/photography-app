package backend.photo.api

import backend.common.api.model.ApiHttpResponse._
import backend.common.api.utils.ApiExceptionsHandler
import backend.common.model.CommonExceptions._
import backend.photo.api.model.ImplicitDtoConversion
import backend.photo.api.model.dtos.PhotoDto
import backend.photo.api.model.enums.CategoryDto
import backend.photo.interactors.PhotoService

import scala.concurrent.{ExecutionContext, Future}

class ApiService(service: PhotoService, validator: ApiValidationService)
                (implicit executionContext: ExecutionContext) extends ApiExceptionsHandler
  with ImplicitDtoConversion {

  @throws[NotFoundException]
  def getPhoto(imageId: String): Future[EnvelopedHttpResponse[PhotoDto]] =
    validator.photoExists(imageId)
      .map(_.toDto)
      .toEnvelopedHttpResponse

  def listPhotos(category: Option[CategoryDto] = None,
                 rating: Option[Int] = None): Future[EnvelopedHttpResponse[Seq[PhotoDto]]] =
    service.listPhotos(category.map(_.toDomain), rating)
      .map(_.map(_.toDto))
      .toEnvelopedHttpResponse

  @throws[BadRequestException]
  def addPhoto(photoDto: PhotoDto): Future[HttpResponse[Unit]] = (for {
    _ <- validator.photoDoesNotExist(photoDto.imageId)
    _ <- service.addPhoto(photoDto.toDomain)
  } yield (): Unit)
    .toHttpResponse

  def removePhoto(imageId: String): Future[HttpResponse[Unit]] = (for {
    _ <- validator.photoExists(imageId)
    _ <- service.removePhoto(imageId)
  } yield (): Unit)
    .toHttpResponse

}
