package backend.image.ports

import backend.image.entities._

import scala.concurrent.Future

trait ImageMetadataRepository {

  def getMetadata(imageId: String): Future[Option[ImageMetadata]]

  def addMetadata(imageId: String, metadata: ImageMetadata): Future[Unit]

  def removeMetadata(imageId: String): Future[Unit]

}
