package backend.photo.adapters.db

import backend.photo.entities.Photo

trait ImplicitPhotoConverter {
  implicit class PhotoDbFromDomain(photo: Photo) {
    def toDbFormat: PhotoDb = PhotoDb(photo.imageId, photo.title)
  }
}
