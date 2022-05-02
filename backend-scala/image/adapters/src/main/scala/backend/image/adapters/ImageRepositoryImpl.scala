package backend.image.adapters

import backend.image.entities.ImageIO
import backend.image.ports.ImageRepository

import scala.concurrent.{ExecutionContext, Future}

object ImageRepositoryImpl {
  def apply()(implicit executionContext: ExecutionContext): ImageRepositoryImpl =
    new ImageRepositoryImpl
}

class ImageRepositoryImpl()(implicit executionContext: ExecutionContext) extends ImageRepository
  with ImageIO {

  def getImageStream(imageId: String): Future[Option[ImageStream]] = ???

  def uploadImageStream(imageStream: ImageUploadStream): Future[Unit] = ???

  def removeImage(imageId: String): Future[Unit] = ???

  def getThumbnailStream(imageId: String): Future[Option[ImageStream]] = ???

  def uploadThumbnailStream(imageStream: ImageUploadStream): Future[Unit] = ???

  def removeThumbnail(imageId: String): Future[Unit] = ???

}
