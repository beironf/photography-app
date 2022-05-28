package backend.photo.adapters.db

import backend.photo.entities.meta.{Coordinates, Location}

case class LocationDb(id: Long,
                      name: String,
                      region: String,
                      country: String,
                      longitude: Float,
                      latitude: Float) {
  def toDomain: Location = Location(name, region, country, Coordinates(longitude, latitude))
}
