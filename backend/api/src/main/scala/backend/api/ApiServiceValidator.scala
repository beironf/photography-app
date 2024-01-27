package backend.api

import backend.common.api.model.ApiHttpErrors.{BadRequest, HttpError, NotFound}
import backend.image.ports.ImageRepository
import backend.photo.entities.Photo
import backend.photo.ports.PhotoRepository
import sttp.model.Part
import sttp.tapir.TapirFile

import scala.concurrent.{ExecutionContext, Future}

class ApiServiceValidator(photoRepository: PhotoRepository,
                          imageRepository: ImageRepository)
                         (implicit executionContext: ExecutionContext) {

  def photoExists(imageId: String): Future[Either[HttpError, Photo]] =
    photoRepository.getPhoto(imageId).map {
      case Some(photo) => Right(photo)
      case _ => Left(NotFound(s"[imageId: $imageId] Photo not found"))
    }

  def photoDoesNotExist(imageId: String): Future[Either[HttpError, Unit]] =
    photoRepository.getPhoto(imageId).map {
      case Some(_) => Left(BadRequest(s"[imageId: $imageId] Photo with imageId already exist"))
      case _ => Right((): Unit)
    }

  def imageExists(imageId: String): Future[Either[HttpError, String]] =
    imageRepository.listImageIds.map(_.contains(imageId)).map {
      case true => Right(imageId)
      case false => Left(NotFound(s"[imageId: $imageId] Image not found"))
    }

  def imageDoesNotExist(imageId: String): Future[Either[HttpError, String]] =
    imageRepository.listImageIds.map(_.contains(imageId)).map {
      case true => Left(BadRequest(s"[imageId: $imageId] Image exists"))
      case false => Right(imageId)
    }

  def fileIsJPG(fileName: String): Either[HttpError, Unit] =
    if (fileName.endsWith(".jpg")) {
      Right((): Unit)
    } else {
      Left(BadRequest(s"[fileName: $fileName] File is not JPG"))
    }

  def fileHasFileName(file: Part[TapirFile]): Either[HttpError, String] =
    file.fileName
      .map(Right(_))
      .getOrElse(Left(BadRequest("File name not found")))

}
