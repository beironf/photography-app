package backend.image.adapters

import backend.image.entities._
import backend.image.ports.ImageMetadataRepository

import scala.concurrent.{ExecutionContext, Future}

object InMemoryImageMetadataRepository {
  def apply()(implicit executionContext: ExecutionContext): InMemoryImageMetadataRepository =
    new InMemoryImageMetadataRepository()
}

class InMemoryImageMetadataRepository()
                                     (implicit executionContext: ExecutionContext) extends ImageMetadataRepository {
  var db: Map[String, ImageMetadata] = Map[String, ImageMetadata]()

  def getMetadata(imageId: String): Future[Option[ImageMetadata]] = Future.successful {
    db.get(imageId)
  }

  def addMetadata(imageId: String, metadata: ImageMetadata): Future[Unit] = Future.successful {
    db += imageId -> metadata
  }

  def removeMetadata(imageId: String): Future[Unit] = Future.successful {
    db -= imageId
  }
}
