package backend.photography.api.model

import backend.photography.entities.photo.meta.{Judgement, Location}
import backend.photography.entities.photo.settings.CameraSettings

import java.time.Instant

case class UpdatePhotoDto(title: String,
                          description: Option[String],
                          photographer: String,
                          group: Option[String],
                          location: Location,
                          taken: Instant,
                          gear: GearDto,
                          cameraSettings: CameraSettings,
                          metadata: MetadataDto,
                          judgement: Judgement)
