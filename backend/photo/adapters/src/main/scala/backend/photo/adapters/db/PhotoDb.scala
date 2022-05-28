package backend.photo.adapters.db

import backend.photo.entities.Photo
import backend.photo.entities.gear.{Camera, Gear, Lens}
import backend.photo.entities.meta.{Judgement, Location, Metadata}
import backend.photo.entities.settings.CameraSettings

import java.sql.Date

case class PhotoDb(imageId: String,
                   title: String,
                   description: Option[String],
                   photographer: String,
                   group: Option[String],
                   locationId: Long,
                   taken: Date,
                   camera: String,
                   lens: String,
                   cameraSettings: CameraSettings,
                   metadata: Metadata,
                   judgementId: Long) {
  def toDomain(location: Location, judgement: Judgement): Photo = Photo(imageId, title, description, photographer,
    group, location, taken.toLocalDate, Gear(Camera.withName(camera), Lens.withName(lens)), cameraSettings, metadata,
    judgement)
}
