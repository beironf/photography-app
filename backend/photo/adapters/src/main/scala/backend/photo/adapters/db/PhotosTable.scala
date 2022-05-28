package backend.photo.adapters.db

import backend.core.sqlstorage.DatabaseConnector
import backend.photo.entities.meta.Metadata
import backend.photo.entities.settings.CameraSettings
import slick.jdbc.JdbcType
import spray.json._

import java.sql.Date

trait PhotosTable extends DatabaseConnector with JsonProtocol {

  import dbConfig.profile.api._

  implicit val jsValueDBFormat: JdbcType[JsValue] = MappedColumnType.base[JsValue, String](_.toString, _.parseJson)
  implicit val cameraSettingsDBFormat: JdbcType[CameraSettings] = MappedColumnType.base[CameraSettings, JsValue](_.toJson, _.convertTo[CameraSettings])
  implicit val metadataDBFormat: JdbcType[Metadata] = MappedColumnType.base[Metadata, JsValue](_.toJson, _.convertTo[Metadata])

  class Photos(tag: Tag) extends Table[PhotoDb](tag, "photos") {

    def imageId: Rep[String] = column[String]("image_id", O.PrimaryKey)
    def title: Rep[String] = column[String]("title")
    def description: Rep[Option[String]] = column[Option[String]]("description")
    def photographer: Rep[String] = column[String]("photographer")
    def group: Rep[Option[String]] = column[Option[String]]("group")
    def locationId: Rep[Long] = column[Long]("locations_id")
    def taken: Rep[Date] = column[Date]("taken")
    def camera: Rep[String] = column[String]("camera")
    def lens: Rep[String] = column[String]("lens")
    def cameraSettings: Rep[CameraSettings] = column[CameraSettings]("camera_settings")
    def metadata: Rep[Metadata] = column[Metadata]("metadata")
    def judgementId: Rep[Long] = column[Long]("judgements_id")

    def * = (imageId, title, description, photographer, group, locationId,
      taken, camera, lens, cameraSettings, metadata, judgementId) <> (PhotoDb.tupled, PhotoDb.unapply)
  }

  val photos = TableQuery[Photos]
}
