package backend.photo.api.model.response

import backend.photo.entities.gear.Gear
import backend.photo.entities.meta.{Judgement, Location, Metadata}
import backend.photo.entities.settings.CameraSettings

import java.time.LocalDate

case class PhotoDto(imageId: String,
                    title: String,
                    description: Option[String],
                    photographer: String,
                    group: Option[String],
                    location: Location,
                    taken: LocalDate,
                    gear: Gear,
                    cameraSettings: CameraSettings,
                    metadata: Metadata,
                    judgement: Judgement)
