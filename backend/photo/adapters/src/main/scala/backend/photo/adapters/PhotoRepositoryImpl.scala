package backend.photo.adapters

import backend.photo.adapters.db._
import backend.photo.entities._
import backend.photo.ports.PhotoRepository
import cats.data.OptionT
import com.rms.miu.slickcats.DBIOInstances._
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import slick.lifted.AbstractTable

import scala.concurrent.Future

object PhotoRepositoryImpl {
  private val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("database")
  def apply(): PhotoRepositoryImpl = new PhotoRepositoryImpl(dbConfig)
}

class PhotoRepositoryImpl(val dbConfig: DatabaseConfig[JdbcProfile]) extends PhotoRepository
  with PhotosTable
  with LocationsTable
  with JudgementsTable
  with ImplicitDbModelConverter {
  import dbConfig.profile.api._

  def getPhoto(imageId: String): Future[Option[Photo]] = db.run {
    (for {
      photoDb <- OptionT(getPhotoDb(imageId))
      locationDb <- OptionT(getLocationDb(photoDb.locationId))
      judgementDb <- OptionT(getJudgementDb(photoDb.judgementId))
    } yield photoDb.toDomain(locationDb.toDomain, judgementDb.toDomain)).value
  }

  def addPhoto(photo: Photo): Future[Unit] = db.run {
    for {
      locationDb <- addLocationDb(photo.location.toDbFormat)
      judgementDb <- addJudgementDb(photo.judgement.toDbFormat)
      _ <- addPhotoDb(photo.toDbFormat(locationDb.id, judgementDb.id))
    } yield (): Unit
  }

  def removePhoto(imageId: String): Future[Unit] = db.run {
    (for {
      photoDb <- OptionT(getPhotoDb(imageId))
      _ <- OptionT.liftF(removeLocationDb(photoDb.locationId))
      _ <- OptionT.liftF(removeJudgementDb(photoDb.judgementId))
      _ <- OptionT.liftF(removePhotoDb(imageId))
    } yield (): Unit).value
      .map(_.getOrElse((): Unit))
  }

  def listPhotos(groupOpt: Option[String] = None,
                 ratingOpt: Option[Int] = None): Future[Seq[Photo]] = db.run {
    photos
      .join(locations).on(_.locationId === _.id)
      .join(judgements).on { case ((p, _), j) => p.judgementId === j.id }
      .filterOpt(groupOpt) { case (((p, _), _), group) => p.group === group }
      .filterOpt(ratingOpt) { case ((_, j), rating) => j.rating === rating }
      .result
      .map(_.map { case ((p, l), j) => p.toDomain(l.toDomain, j.toDomain) })
  }


  private def getPhotoDb(imageId: String): DBIO[Option[PhotoDb]] = get[Photos](photos, _.imageId === imageId)
  private def addPhotoDb(photoDb: PhotoDb): DBIO[PhotoDb] = add[Photos](photos, photoDb)
  private def removePhotoDb(imageId: String): DBIO[Int] = photos.filter(_.imageId === imageId).delete

  private def getLocationDb(locationId: Long): DBIO[Option[LocationDb]] = get[Locations](locations, _.id === locationId)
  private def addLocationDb(locationDb: LocationDb): DBIO[LocationDb] = add[Locations](locations, locationDb)
  private def removeLocationDb(locationId: Long): DBIO[Int] = locations.filter(_.id === locationId).delete

  private def getJudgementDb(judgementId: Long): DBIO[Option[JudgementDb]] = get[Judgements](judgements, _.id === judgementId)
  private def addJudgementDb(judgementDb: JudgementDb): DBIO[JudgementDb] = add[Judgements](judgements, judgementDb)
  private def removeJudgementDb(judgementId: Long): DBIO[Int] = judgements.filter(_.id === judgementId).delete


  private def get[T <: AbstractTable[_]](table: TableQuery[T],
                                         filterCondition: T => Rep[Boolean]): DBIO[Option[T#TableElementType]] =
    table.filter(filterCondition).result.headOption

  private def add[T <: AbstractTable[_]](table: TableQuery[T],
                                         element: T#TableElementType): DBIO[T#TableElementType] =
    table returning table += element

}
