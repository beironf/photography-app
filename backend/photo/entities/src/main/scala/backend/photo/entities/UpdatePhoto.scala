package backend.photo.entities

import backend.photo.entities.gear.Gear
import backend.photo.entities.meta.{Judgement, Location, Metadata}
import backend.photo.entities.settings.CameraSettings

import java.time.Instant

case class UpdatePhoto(title: String,
                       description: Option[String],
                       photographer: String,
                       group: Option[String],
                       location: Location,
                       taken: Instant,
                       gear: Gear,
                       cameraSettings: CameraSettings,
                       metadata: Metadata,
                       judgement: Judgement) {
  def toPhoto(imageId: String): Photo = Photo(
    imageId, title, description, photographer, group, location, taken, gear, cameraSettings, metadata, judgement
  )
}
