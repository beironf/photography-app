package backend.image.interactors

import backend.image.entities.ImageExif
import backend.image.ports.ImageExifRepository

import scala.concurrent.Future

class ImageExifService(repository: ImageExifRepository) {

  def getExif(imageId: String): Future[Option[ImageExif]] =
    repository.getExif(imageId)

  def addExif(imageId: String, exif: ImageExif): Future[Unit] =
    repository.addExif(imageId, exif)

  def removeExif(imageId: String): Future[Unit] =
    repository.removeExif(imageId)

}