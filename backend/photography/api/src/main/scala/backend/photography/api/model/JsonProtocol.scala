package backend.photography.api.model

import backend.common.json.JsonSupport
import backend.photography.api.model.enums.*
import backend.photography.entities.photo.meta.{Coordinates, Judgement, Location}
import backend.photography.entities.photo.settings.CameraSettings
import spray.json.*

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
  implicit val updatePhotoDtoFormat: RootJsonFormat[UpdatePhotoDto] = jsonFormat10(UpdatePhotoDto)
  implicit val photoWithRatioDtoFormat: RootJsonFormat[PhotoWithRatioDto] = jsonFormat3(PhotoWithRatioDto)
  implicit val exifFormat: RootJsonFormat[ImageExifDto] = jsonFormat10(ImageExifDto)
  implicit val imageFormat: RootJsonFormat[ImageDto] = jsonFormat3(ImageDto)
}
