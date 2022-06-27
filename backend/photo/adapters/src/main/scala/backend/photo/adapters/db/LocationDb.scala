package backend.photo.adapters.db

import backend.photo.entities.meta.{Coordinates, Location}

case class LocationDb(id: Long,
                      name: String,
                      country: String,
                      longitude: Float,
                      latitude: Float) {
  def toDomain: Location = Location(name, country, Coordinates(longitude, latitude))
}
