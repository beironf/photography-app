package backend.photography.interactors

import backend.core.utils.EitherTExtensions
import backend.photography.entities.exif.ImageExif
import backend.photography.entities.response.Exceptions.PhotographyException
import backend.photography.entities.response.Response.PhotographyResponse
import backend.photography.interactors.validation.Validator
import backend.photography.ports.ImageExifRepository

import scala.concurrent.{ExecutionContext, Future}

object ImageExifService {
  def apply(validator: Validator,
            repository: ImageExifRepository)
           (implicit executionContext: ExecutionContext): ImageExifService =
    new ImageExifService(validator, repository)
}

class ImageExifService(validator: Validator,
                       repository: ImageExifRepository)
                      (implicit executionContext: ExecutionContext) extends EitherTExtensions {

  def getExif(imageId: String): Future[PhotographyResponse[ImageExif]] = (for {
    exif <- validator.exifExists(imageId).toEitherT
  } yield exif).value

  def listExif(imageIds: Option[Seq[String]] = None): Future[PhotographyResponse[Seq[(String, ImageExif)]]] =
    repository.listExif(imageIds).toEitherT[PhotographyException].value

}
