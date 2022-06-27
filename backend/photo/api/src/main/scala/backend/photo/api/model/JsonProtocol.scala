package backend.photo.api.model

import backend.common.json.JsonSupport
import backend.photo.api.model.dtos._
import backend.photo.api.model.enums._
import backend.photo.entities.meta._
import backend.photo.entities.settings.CameraSettings
import spray.json._

trait JsonProtocol extends JsonSupport {
  implicit val judgementFormat: RootJsonFormat[Judgement] = jsonFormat2(Judgement)
  implicit val cameraTechniqueDtoFormat: RootJsonFormat[CameraTechniqueDto] = jsonFormatIdentityString[CameraTechniqueDto](CameraTechniqueDto.apply, _.name)
  implicit val categoryDtoFormat: RootJsonFormat[CategoryDto] = jsonFormatIdentityString[CategoryDto](CategoryDto.apply, _.name)
  implicit val metadataDtoFormat: RootJsonFormat[MetadataDto] = jsonFormat3(MetadataDto)
  implicit val cameraSettingsFormat: RootJsonFormat[CameraSettings] = jsonFormat4(CameraSettings)
  implicit val lensDtoFormat: RootJsonFormat[LensDto] = jsonFormatIdentityString[LensDto](LensDto.apply, _.name)
  implicit val cameraDtoFormat: RootJsonFormat[CameraDto] = jsonFormatIdentityString[CameraDto](CameraDto.apply, _.name)
  implicit val gearFormat: RootJsonFormat[GearDto] = jsonFormat2(GearDto)
  implicit val coordinatesFormat: RootJsonFormat[Coordinates] = jsonFormat2(Coordinates)
  implicit val locationFormat: RootJsonFormat[Location] = jsonFormat3(Location)
  implicit val photoDtoFormat: RootJsonFormat[PhotoDto] = jsonFormat11(PhotoDto)
}
