package backend.exif.adapters

import backend.core.sqlstorage.DatabaseConnector
import backend.core.sqlstorage.postgres.PostgresMaterializerDBIO
import backend.exif.adapters.exifdb.*
import backend.exif.entities.ImageExif
import backend.exif.ports.ImageExifRepository

import scala.concurrent.{ExecutionContext, Future}

object ImageExifRepositoryImpl {
  private val dbConnector = DatabaseConnector.defaultPostgresConnector
  implicit val executionContext: ExecutionContext = DatabaseConnector.executionContext
  def apply(): ImageExifRepositoryImpl = new ImageExifRepositoryImpl(PostgresMaterializerDBIO(dbConnector), dbConnector)
}

class ImageExifRepositoryImpl(db: PostgresMaterializerDBIO,
                              val databaseConnector: DatabaseConnector)
                             (implicit executionContext: ExecutionContext)
  extends ImageExifRepository
    with ImagesExifTable
    with ImplicitImageExifConverter {
  import databaseConnector.profile.api.*

  def getExif(imageId: String): Future[Option[ImageExif]] = db.run {
    imagesExif.filter(_.imageId === imageId).result.headOption
  }.map(_.map(_.toDomain))

  def listExif(imageIds: Option[Seq[String]] = None): Future[Seq[(String, ImageExif)]] = db.run {
    imagesExif.filterOpt(imageIds)(_.imageId inSet _).result
  }.map(_.map(e => (e.imageId, e.toDomain)))

  def addExif(imageId: String, exif: ImageExif): Future[Unit] = db.run {
    imagesExif += exif.toDbFormat(imageId)
  }.map(_ => (): Unit)

  def removeExif(imageId: String): Future[Unit] = db.run {
    imagesExif.filter(_.imageId === imageId).delete
  }.map(_ => (): Unit)

}
