package backend.photo.api

import backend.common.api.model.ApiHttpErrors._
import backend.photo.entities._
import backend.photo.ports.PhotoRepository

import scala.concurrent.{ExecutionContext, Future}

class ApiValidationService(repository: PhotoRepository)
                          (implicit executionContext: ExecutionContext) {

  def photoExists(imageId: String): Future[Either[HttpError, Photo]] =
    repository.getPhoto(imageId).map {
      case Some(photo) => Right(photo)
      case _ => Left(NotFound(s"[imageId: $imageId] Photo not found"))
    }

  def photoDoesNotExist(imageId: String): Future[Either[HttpError, Unit]] =
    repository.getPhoto(imageId).map {
      case Some(_) => Left(BadRequest(s"[imageId: $imageId] Photo with imageId already exist"))
      case _ => Right((): Unit)
    }

}
