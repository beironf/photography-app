package backend.image.interactors

import backend.image.entities.ImageIO
import backend.image.ports.ImageRepository

import java.io.File
import scala.concurrent.Future

class ImageService(repository: ImageRepository) extends ImageIO {

  def getImageStream(imageId: String): Future[Option[ImageStream]] =
    repository.getImageStream(imageId)

  def uploadImageStream(imageFile: File): Future[Unit] =
    repository.uploadImage(imageFile)

  def removeImage(imageId: String): Future[Unit] =
    repository.removeImage(imageId)

  def getThumbnailStream(imageId: String): Future[Option[ImageStream]] =
    repository.getThumbnailStream(imageId)

  def uploadThumbnail(thumbnailFile: File): Future[Unit] =
    repository.uploadThumbnail(thumbnailFile)

  def removeThumbnail(imageId: String): Future[Unit] =
    repository.removeThumbnail(imageId)

}
