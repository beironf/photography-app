package backend.image.interactors

import backend.image.entities.ImageIO
import backend.image.ports.ImageRepository

import scala.concurrent.Future

class ImageService(repository: ImageRepository) extends ImageIO {

  def getImageStream(imageId: String): Future[Option[ImageStream]] =
    repository.getImageStream(imageId)

  def uploadImageStream(imageStream: ImageUploadStream): Future[Unit] =
    repository.uploadImageStream(imageStream)

  def removeImage(imageId: String): Future[Unit] =
    repository.removeImage(imageId)

  def getThumbnailStream(imageId: String): Future[Option[ImageStream]] =
    repository.getThumbnailStream(imageId)

  def uploadThumbnailStream(imageStream: ImageUploadStream): Future[Unit] =
    repository.uploadThumbnailStream(imageStream)

  def removeThumbnail(imageId: String): Future[Unit] =
    repository.removeThumbnail(imageId)

}
