package backend.photography.ports.repositories

import backend.photography.entities.exif.ImageExif

import scala.concurrent.Future

trait ImageExifRepository {

  def getExif(imageId: String): Future[Option[ImageExif]]

  def listExif(imageIds: Option[Seq[String]] = None): Future[Seq[(String, ImageExif)]]

  def addExif(imageId: String, exif: ImageExif): Future[Unit]

  def removeExif(imageId: String): Future[Unit]

}
