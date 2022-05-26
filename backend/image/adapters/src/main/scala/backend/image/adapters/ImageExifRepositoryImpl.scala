package backend.image.adapters

import backend.image.adapters.exifdb._
import backend.image.entities.ImageExif
import backend.image.ports.ImageExifRepository
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

object ImageExifRepositoryImpl {
  private val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("database")
  def apply(): ImageExifRepository = new ImageExifRepositoryImpl(dbConfig)
}

class ImageExifRepositoryImpl(val dbConfig: DatabaseConfig[JdbcProfile]) extends ImageExifRepository
  with ImagesExifTable with ImplicitImageExifConverter {
  import dbConfig.profile.api._

  def getExif(imageId: String): Future[Option[ImageExif]] = db.run {
    imagesExif.filter(_.imageId === imageId).result.headOption
  }.map(_.map(_.toDomain))

  def addExif(imageId: String, exif: ImageExif): Future[Unit] = db.run {
    imagesExif += exif.toDbFormat(imageId)
  }.map(_ => (): Unit)

  def removeExif(imageId: String): Future[Unit] = db.run {
    imagesExif.filter(_.imageId === imageId).delete
  }.map(_ => (): Unit)

}
