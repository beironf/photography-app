package backend.photography.repositories.model.photo

import backend.common.json.JsonSupport
import backend.photography.entities.photo.meta.CameraTechnique.CameraTechnique
import backend.photography.entities.photo.meta.Category.Category
import backend.photography.entities.photo.meta.*
import backend.photography.entities.photo.settings.CameraSettings
import spray.json.RootJsonFormat

trait JsonProtocol extends JsonSupport {
  implicit val categoryFormat: RootJsonFormat[Category] = jsonFormatIdentityString[Category](Category.withName, _.toString)
  implicit val cameraTechniqueFormat: RootJsonFormat[CameraTechnique] = jsonFormatIdentityString[CameraTechnique](CameraTechnique.withName, _.toString)
  implicit val metadataFormat: RootJsonFormat[Metadata] = jsonFormat3(Metadata)
  implicit val cameraSettingsFormat: RootJsonFormat[CameraSettings] = jsonFormat4(CameraSettings)
}
