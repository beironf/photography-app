package backend.image.ports

import backend.image.entities.ImageIO

import java.io.File
import scala.concurrent.Future

trait ImageRepository extends ImageIO {

  def getImageStream(imageId: String): Future[Option[ImageStream]]

  def uploadImage(file: File): Future[Unit]

  def removeImage(imageId: String): Future[Unit]

  def getThumbnailStream(imageId: String): Future[Option[ImageStream]]

  def uploadThumbnail(file: File): Future[Unit]

  def removeThumbnail(imageId: String): Future[Unit]

}
