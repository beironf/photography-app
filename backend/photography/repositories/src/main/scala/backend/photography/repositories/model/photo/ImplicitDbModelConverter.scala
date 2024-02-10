package backend.photography.repositories.model.photo

import backend.photography.entities.photo.{Photo, UpdatePhoto}
import backend.photography.entities.photo.meta.{Judgement, Location}

import java.sql.Timestamp

trait ImplicitDbModelConverter {
  implicit class PhotoDbFromDomain(photo: Photo) {
    def toDbFormat(locationId: Long, judgementId: Long): PhotoDb = PhotoDb(photo.imageId, photo.title, photo.description,
      photo.photographer, photo.group, locationId, Timestamp.from(photo.taken), photo.gear.camera.toString,
      photo.gear.lens.toString, photo.cameraSettings, photo.metadata, judgementId)
  }

  implicit class UpdatePhotoDbFromDomain(update: UpdatePhoto) {
    def toDbFormat: UpdatePhotoDb = UpdatePhotoDb(update.title, update.description,
      update.photographer, update.group, Timestamp.from(update.taken), update.gear.camera.toString,
      update.gear.lens.toString, update.cameraSettings, update.metadata)
  }

  implicit class LocationDbFromDomain(location: Location) {
    def toDbFormat: LocationDb = LocationDb(-1, location.name, location.country, location.coordinates.longitude,
      location.coordinates.latitude)
  }

  implicit class JudgementDbFromDomain(judgement: Judgement) {
    def toDbFormat: JudgementDb = JudgementDb(-1, judgement.rating, judgement.inShowroom)
  }
}
