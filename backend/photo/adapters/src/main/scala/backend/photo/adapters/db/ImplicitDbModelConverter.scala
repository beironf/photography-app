package backend.photo.adapters.db

import backend.photo.entities.Photo
import backend.photo.entities.meta.{Judgement, Location}

import java.sql.Date

trait ImplicitDbModelConverter {
  implicit class PhotoDbFromDomain(photo: Photo) {
    def toDbFormat(locationId: Long, judgementId: Long): PhotoDb = PhotoDb(photo.imageId, photo.title, photo.description,
      photo.photographer, photo.group, locationId, Date.valueOf(photo.taken), photo.gear.camera.toString,
      photo.gear.lens.toString, photo.cameraSettings, photo.metadata, judgementId)
  }

  implicit class LocationDbFromDomain(location: Location) {
    def toDbFormat: LocationDb = LocationDb(-1, location.name, location.region, location.country,
      location.coordinates.longitude, location.coordinates.latitude)
  }

  implicit class JudgementDbFromDomain(judgement: Judgement) {
    def toDbFormat: JudgementDb = JudgementDb(-1, judgement.rating, judgement.inShowroom)
  }
}
