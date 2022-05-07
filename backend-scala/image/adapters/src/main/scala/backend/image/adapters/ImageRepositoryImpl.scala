package backend.image.adapters

import backend.image.entities.ImageIO
import backend.image.ports.ImageRepository

import java.io.File
import scala.concurrent.{ExecutionContext, Future}

object ImageRepositoryImpl {
  def apply()(implicit executionContext: ExecutionContext): ImageRepositoryImpl =
    new ImageRepositoryImpl
}

class ImageRepositoryImpl()(implicit executionContext: ExecutionContext) extends ImageRepository
  with ImageIO {

  def getImageStream(imageId: String): Future[Option[ImageStream]] = ???

  def uploadImage(file: File): Future[Unit] = ???

  def removeImage(imageId: String): Future[Unit] = ???

  def getThumbnailStream(imageId: String): Future[Option[ImageStream]] = ???

  def uploadThumbnail(file: File): Future[Unit] = ???

  def removeThumbnail(imageId: String): Future[Unit] = ???

}
