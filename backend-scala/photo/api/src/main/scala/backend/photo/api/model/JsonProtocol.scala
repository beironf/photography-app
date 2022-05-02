package backend.photo.api.model

import backend.common.json.JsonSupport
import backend.photo.api.model.request.AddPhoto
import backend.photo.api.model.response.PhotoDto
import backend.photo.entities.gear._
import backend.photo.entities.meta._
import backend.photo.entities.settings.CameraSettings
import spray.json._

trait JsonProtocol extends JsonSupport {
  implicit val judgementFormat: RootJsonFormat[Judgement] = jsonFormat2(Judgement)
  implicit val cameraTechniquesFormat: RootJsonFormat[CameraTechnique] = jsonFormat1(CameraTechnique.apply)
  implicit val personFormat: RootJsonFormat[Person] = jsonFormat2(Person)
  implicit val categoryDtoFormat: RootJsonFormat[CategoryDto] = jsonFormatIdentityString[CategoryDto](CategoryDto.apply, _.name)
  implicit val metadataFormat: RootJsonFormat[Metadata] = jsonFormat4(Metadata)
  implicit val cameraSettingsFormat: RootJsonFormat[CameraSettings] = jsonFormat4(CameraSettings)
  implicit val lensFormat: RootJsonFormat[Lens] = jsonFormat1(Lens.apply)
  implicit val cameraFormat: RootJsonFormat[Camera] = jsonFormat1(Camera.apply)
  implicit val gearFormat: RootJsonFormat[Gear] = jsonFormat2(Gear)
  implicit val coordinatesFormat: RootJsonFormat[Coordinates] = jsonFormat2(Coordinates)
  implicit val locationFormat: RootJsonFormat[Location] = jsonFormat4(Location)
  implicit val photoDtoFormat: RootJsonFormat[PhotoDto] = jsonFormat11(PhotoDto)
  implicit val addPhotoFormat: RootJsonFormat[AddPhoto] = jsonFormat11(AddPhoto)
}
