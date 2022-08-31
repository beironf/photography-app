package backend.image.api

import backend.common.api.model.ApiHttpErrors.{BadRequest, HttpError, NotFound}
import backend.image.interactors.ImageService
import sttp.model.Part
import sttp.tapir.TapirFile

import scala.concurrent.{ExecutionContext, Future}

class ApiServiceValidator(imageService: ImageService)
                         (implicit executionContext: ExecutionContext) {

  def imageExists(imageId: String): Future[Either[HttpError, String]] =
    imageService.listImageIds.map(_.contains(imageId)).map {
      case true => Right(imageId)
      case false => Left(NotFound(s"[imageId: $imageId] Image not found"))
    }

  def imageDoesNotExist(imageId: String): Future[Either[HttpError, String]] =
    imageService.listImageIds.map(_.contains(imageId)).map {
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
