package backend.image.ports

import backend.image.entities._

import scala.concurrent.Future

trait ImageExifRepository {

  def getExif(imageId: String): Future[Option[ImageExif]]

  def addExif(imageId: String, exif: ImageExif): Future[Unit]

  def removeExif(imageId: String): Future[Unit]

}
