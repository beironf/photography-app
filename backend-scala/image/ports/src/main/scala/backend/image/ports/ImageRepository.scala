package backend.image.ports

import backend.image.entities.ImageIO

import scala.concurrent.Future

trait ImageRepository extends ImageIO {

  def getImageStream(imageId: String): Future[Option[ImageStream]]

  def uploadImageStream(imageStream: ImageUploadStream): Future[Unit]

  def removeImage(imageId: String): Future[Unit]

  def getThumbnailStream(imageId: String): Future[Option[ImageStream]]

  def uploadThumbnailStream(imageStream: ImageUploadStream): Future[Unit]

  def removeThumbnail(imageId: String): Future[Unit]

}
