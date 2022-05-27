package backend.photo.adapters

import backend.photo.adapters.db.{ImplicitPhotoConverter, PhotosTable}
import backend.photo.entities._
import backend.photo.entities.meta._
import backend.photo.ports.PhotoRepository
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

object PhotoRepositoryImpl {
  private val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("database")
  def apply(): PhotoRepositoryImpl = new PhotoRepositoryImpl(dbConfig)
}

class PhotoRepositoryImpl(val dbConfig: DatabaseConfig[JdbcProfile]) extends PhotoRepository
  with PhotosTable with ImplicitPhotoConverter {
  import dbConfig.profile.api._

  def getPhoto(imageId: String): Future[Option[Photo]] = db.run {
    photos.filter(_.imageId === imageId).result.headOption
  }.map(_.map(_.toDomain))

  def addPhoto(photo: Photo): Future[Unit] = db.run {
    photos += photo.toDbFormat
  }.map(_ => (): Unit)

  def removePhoto(imageId: String): Future[Unit] = db.run {
    photos.filter(_.imageId === imageId).delete
  }.map(_ => (): Unit)

  def listPhotos(category: Option[Category.Value] = None,
                 rating: Option[Int] = None): Future[Seq[Photo]] = db.run {
    photos
      //.filterOpt(category)(_.) // TODO: filter on category
      //.filterOpt(rating)(_.) // TODO: filter on rating
      .result
  }.map(_.map(_.toDomain))

}
