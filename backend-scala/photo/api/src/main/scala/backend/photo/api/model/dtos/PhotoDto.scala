package backend.photo.api.model.dtos

import backend.photo.entities.meta._
import backend.photo.entities.settings._

import java.time.LocalDate

case class PhotoDto(imageId: String,
                    title: String,
                    description: Option[String],
                    photographer: String,
                    group: Option[String],
                    location: Location,
                    taken: LocalDate,
                    gear: GearDto,
                    cameraSettings: CameraSettings,
                    metadata: MetadataDto,
                    judgement: Judgement)
