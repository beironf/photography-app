package backend.image.interactors

import backend.image.entities.ImageMetadata
import backend.image.ports.ImageMetadataRepository

import scala.concurrent.Future

class ImageMetadataService(repository: ImageMetadataRepository) {

  def getMetadata(imageId: String): Future[Option[ImageMetadata]] =
    repository.getMetadata(imageId)

  def addMetadata(imageId: String, metadata: ImageMetadata): Future[Unit] =
    repository.addMetadata(imageId, metadata)

  def removeMetadata(imageId: String): Future[Unit] =
    repository.removeMetadata(imageId)

}
