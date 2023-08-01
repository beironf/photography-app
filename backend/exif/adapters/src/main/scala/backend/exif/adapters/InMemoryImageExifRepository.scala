package backend.exif.adapters

import backend.exif.entities.*
import backend.exif.ports.ImageExifRepository

import scala.concurrent.Future

object InMemoryImageExifRepository {
  def apply(): InMemoryImageExifRepository =
    new InMemoryImageExifRepository()
}

class InMemoryImageExifRepository() extends ImageExifRepository {
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
