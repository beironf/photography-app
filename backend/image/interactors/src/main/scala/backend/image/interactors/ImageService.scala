package backend.image.interactors

import backend.image.entities.ImageIO
import backend.image.ports.ImageRepository

import scala.concurrent.Future

class ImageService(repository: ImageRepository) extends ImageIO {

  def getImageNames: Future[Seq[String]] =
    repository.getImageNames

  def getImageStream(imageId: String): Future[Option[ImageStream]] =
    repository.getImageStream(imageId)

  def uploadImage(fileName: String, bytes: Array[Byte]): Future[Unit] =
    repository.uploadImage(fileName, bytes)

  def removeImage(imageId: String): Future[Unit] =
    repository.removeImage(imageId)

  def getThumbnailStream(imageId: String): Future[Option[ImageStream]] =
    repository.getThumbnailStream(imageId)

  def uploadThumbnail(fileName: String, bytes: Array[Byte]): Future[Unit] =
    repository.uploadThumbnail(fileName, bytes)

  def removeThumbnail(imageId: String): Future[Unit] =
    repository.removeThumbnail(imageId)

}
