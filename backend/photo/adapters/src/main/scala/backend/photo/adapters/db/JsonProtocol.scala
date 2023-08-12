package backend.photo.adapters.db

import backend.common.json.JsonSupport
import backend.photo.entities.meta.CameraTechnique.CameraTechnique
import backend.photo.entities.meta.Category.Category
import backend.photo.entities.meta.*
import backend.photo.entities.settings.CameraSettings
import spray.json.RootJsonFormat

trait JsonProtocol extends JsonSupport {
  implicit val categoryFormat: RootJsonFormat[Category] = jsonFormatIdentityString[Category](Category.withName, _.toString)
  implicit val cameraTechniqueFormat: RootJsonFormat[CameraTechnique] = jsonFormatIdentityString[CameraTechnique](CameraTechnique.withName, _.toString)
  implicit val metadataFormat: RootJsonFormat[Metadata] = jsonFormat3(Metadata)
  implicit val cameraSettingsFormat: RootJsonFormat[CameraSettings] = jsonFormat4(CameraSettings)
}
