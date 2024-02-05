package backend.photography.interactors

import backend.photography.entities.photo.Photo
import backend.photography.entities.response.Exceptions.PhotoNotFoundException
import backend.photography.entities.response.Response.PhotographyResponse
import backend.photography.ports.{ImageRepository, PhotoRepository}
import cats.implicits.catsSyntaxEitherId

import scala.concurrent.{ExecutionContext, Future}

class Validator(photoRepository: PhotoRepository,
                imageRepository: ImageRepository)
               (implicit executionContext: ExecutionContext) {

  def photoExists(imageId: String): Future[PhotographyResponse[Photo]] =
    photoRepository.getPhoto(imageId).map {
      case Some(photo) => photo.asRight
      case _ => PhotoNotFoundException(imageId).asLeft
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
