package backend.image.adapters

import backend.image.entities._
import backend.image.ports.ImageExifRepository

import scala.concurrent.{ExecutionContext, Future}

object InMemoryImageExifRepository {
  def apply()(implicit executionContext: ExecutionContext): InMemoryImageExifRepository =
    new InMemoryImageExifRepository()
}

class InMemoryImageExifRepository()
                                 (implicit executionContext: ExecutionContext) extends ImageExifRepository {
  var db: Map[String, ImageExif] = Map[String, ImageExif]()

  def getExif(imageId: String): Future[Option[ImageExif]] = Future.successful {
    db.get(imageId)
  }

  def listExif(imageIds: Option[Seq[String]] = None): Future[Seq[(String, ImageExif)]] = Future.successful {
    db.view.filterKeys(imageId => imageIds.forall(_.contains(imageId))).toSeq
  }

  def addExif(imageId: String, exif: ImageExif): Future[Unit] = Future.successful {
    db += imageId -> exif
  }

  def removeExif(imageId: String): Future[Unit] = Future.successful {
    db -= imageId
  }
}
