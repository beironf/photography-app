package backend.photography.interactors

import backend.photography.entities.image.ImageIO
import backend.photography.ports.ImageRepository

import scala.concurrent.Future

object ImageService {
  def apply(validator: Validator,
            repository: ImageRepository): ImageService =
    new ImageService(validator, repository)
}

class ImageService(validator: Validator,
                   repository: ImageRepository) extends ImageIO {

  def listImageIds: Future[Seq[String]] =
    repository.listImageIds

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

  def getSiteImageStream(fileName: String): Future[Option[ImageStream]] =
    repository.getSiteImageStream(fileName)

}
