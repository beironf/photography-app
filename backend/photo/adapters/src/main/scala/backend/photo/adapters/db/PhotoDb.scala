package backend.photo.adapters.db

import backend.photo.entities.Photo

case class PhotoDb(imageId: String,
                   title: String) {
  def toDomain: Photo = ???
}
