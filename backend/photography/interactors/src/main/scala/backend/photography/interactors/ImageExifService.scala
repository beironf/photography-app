package backend.photography.interactors

import backend.photography.entities.exif.ImageExif
import backend.photography.ports.ImageExifRepository

import scala.concurrent.Future

object ImageExifService {
  def apply(validator: Validator,
            repository: ImageExifRepository): ImageExifService =
    new ImageExifService(validator, repository)
}

class ImageExifService(validator: Validator,
                       repository: ImageExifRepository) {

  def getExif(imageId: String): Future[Option[ImageExif]] =
    repository.getExif(imageId)

  def listExif(imageIds: Option[Seq[String]] = None): Future[Seq[(String, ImageExif)]] =
    repository.listExif(imageIds)

  def addExif(imageId: String, exif: ImageExif): Future[Unit] =
    repository.addExif(imageId, exif)

  def removeExif(imageId: String): Future[Unit] =
    repository.removeExif(imageId)

}
