package backend.photo.adapters

import backend.photo.adapters.db._
import backend.photo.entities._
import backend.photo.entities.meta.Category.Category
import backend.photo.entities.meta.{Judgement, Location}
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
      locationId <- addLocationDb(photo.location.toDbFormat)
      judgementId <- addJudgementDb(photo.judgement.toDbFormat)
      _ <- addPhotoDb(photo.toDbFormat(locationId, judgementId))
    } yield (): Unit
  }

  def updatePhoto(imageId: String, update: UpdatePhoto): Future[Unit] = db.run {
    (for {
      photoDb <- OptionT(getPhotoDb(imageId))
      _ <- OptionT.liftF(updatePhotoDb(imageId, update.toDbFormat))
      _ <- OptionT.liftF(updateLocationDb(photoDb.locationId, update.location))
      _ <- OptionT.liftF(updateJudgementDb(photoDb.judgementId, update.judgement))
    } yield (): Unit).value
      .map(_.getOrElse((): Unit))
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

  def listPhotos(categoryOpt: Option[Category] = None,
                 groupOpt: Option[String] = None,
                 ratingOpt: Option[Int] = None,
                 inShowroomOpt: Option[Boolean] = None): Future[Seq[Photo]] = db.run {
    photos
      .join(locations).on(_.locationId === _.id)
      .join(judgements).on { case ((p, _), j) => p.judgementId === j.id }
      .filterOpt(groupOpt) { case (((p, _), _), group) => p.group === group }
      .filterOpt(ratingOpt) { case ((_, j), rating) => j.rating === rating }
      .filterOpt(inShowroomOpt) { case ((_, j), inShowroom) => j.inShowroom === inShowroom }
      .result
      .map(_
        .filter { case ((p, _), _) => categoryOpt.forall(_ == p.metadata.category) }
        .map { case ((p, l), j) => p.toDomain(l.toDomain, j.toDomain) }
      )
  }

  def listPhotoGroups: Future[Seq[String]] = db.run {
    photos
      .filter(_.group.nonEmpty)
      .map(_.group)
      .result
      .map(_.flatten.distinct)
  }


  private def getPhotoDb(imageId: String): DBIO[Option[PhotoDb]] = get[Photos](photos, _.imageId === imageId)
  private def addPhotoDb(photoDb: PhotoDb): DBIO[Int] = photos += photoDb
  private def removePhotoDb(imageId: String): DBIO[Int] = photos.filter(_.imageId === imageId).delete
  private def updatePhotoDb(imageId: String, u: UpdatePhotoDb): DBIO[Int] = photos
    .filter(_.imageId === imageId)
    .map(p => (p.title, p.description, p.photographer, p.group, p.taken, p.camera, p.lens, p.cameraSettings, p.metadata))
    .update(UpdatePhotoDb.unapply(u).get)

  private def getLocationDb(locationId: Long): DBIO[Option[LocationDb]] = get[Locations](locations, _.id === locationId)
  private def addLocationDb(locationDb: LocationDb): DBIO[Long] = locations returning locations.map(_.id) += locationDb
  private def removeLocationDb(locationId: Long): DBIO[Int] = locations.filter(_.id === locationId).delete
  private def updateLocationDb(locationId: Long, location: Location): DBIO[Int] = locations
    .filter(_.id === locationId)
    .map(l => (l.name, l.country, l.latitude, l.longitude))
    .update((location.name, location.country, location.coordinates.latitude, location.coordinates.longitude))

  private def getJudgementDb(judgementId: Long): DBIO[Option[JudgementDb]] = get[Judgements](judgements, _.id === judgementId)
  private def addJudgementDb(judgementDb: JudgementDb): DBIO[Long] = judgements returning judgements.map(_.id) += judgementDb
  private def removeJudgementDb(judgementId: Long): DBIO[Int] = judgements.filter(_.id === judgementId).delete
  private def updateJudgementDb(judgementId: Long, judgement: Judgement): DBIO[Int] = judgements
    .filter(_.id === judgementId)
    .map(j => (j.rating, j.inShowroom))
    .update((judgement.rating, judgement.inShowroom))


  private def get[T <: AbstractTable[_]](table: TableQuery[T],
                                         filterCondition: T => Rep[Boolean]): DBIO[Option[T#TableElementType]] =
    table.filter(filterCondition).result.headOption

}
