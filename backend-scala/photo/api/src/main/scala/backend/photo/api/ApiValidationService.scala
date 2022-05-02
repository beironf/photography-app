package backend.photo.api

import backend.common.model.CommonExceptions._
import backend.photo.entities._
import backend.photo.ports.PhotoRepository

import scala.concurrent.{ExecutionContext, Future}

class ApiValidationService(repository: PhotoRepository)
                          (implicit executionContext: ExecutionContext) {

  @throws[NotFoundException]
  def photoExists(imageId: String): Future[Photo] =
    repository.getPhoto(imageId).map{
      case Some(photo) => photo
      case _ => throw NotFoundException(s"[imageId: $imageId] Photo not found")
    }

  @throws[BadRequestException]
  def photoDoesNotExist(imageId: String): Future[Unit] =
    repository.getPhoto(imageId).map{
      case Some(_) => throw BadRequestException(s"[imageId: $imageId] Photo with imageId already exist")
      case _ => (): Unit
    }

}
