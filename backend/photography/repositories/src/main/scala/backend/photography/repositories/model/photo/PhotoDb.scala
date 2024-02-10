package backend.photography.repositories.model.photo

import backend.photography.entities.photo.Photo
import backend.photography.entities.photo.gear.{Camera, Gear, Lens}
import backend.photography.entities.photo.meta.{Judgement, Location, Metadata}
import backend.photography.entities.photo.settings.CameraSettings

import java.sql.Timestamp

case class PhotoDb(imageId: String,
                   title: String,
                   description: Option[String],
                   photographer: String,
                   group: Option[String],
                   locationId: Long,
                   taken: Timestamp,
                   camera: String,
                   lens: String,
                   cameraSettings: CameraSettings,
                   metadata: Metadata,
                   judgementId: Long) {
  def toDomain(location: Location, judgement: Judgement): Photo = Photo(imageId, title, description, photographer,
    group, location, taken.toInstant, Gear(Camera.withName(camera), Lens.withName(lens)), cameraSettings, metadata,
    judgement)
}
