package backend.image.ports

import backend.image.entities.ImageIO

import scala.concurrent.Future

trait ImageRepository extends ImageIO {

  def listImageIds: Future[Seq[String]]

  def getImageStream(imageId: String): Future[Option[ImageStream]]

  def uploadImage(fileName: String, bytes: Array[Byte]): Future[Unit]

  def removeImage(imageId: String): Future[Unit]

  def getThumbnailStream(imageId: String): Future[Option[ImageStream]]

  def uploadThumbnail(fileName: String, bytes: Array[Byte]): Future[Unit]

  def removeThumbnail(imageId: String): Future[Unit]

}
