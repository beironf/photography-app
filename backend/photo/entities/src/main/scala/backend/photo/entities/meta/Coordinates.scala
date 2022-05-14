package backend.photo.entities.meta

case class Coordinates(longitude: Int,
                       latitude: Int) {
  require(longitude >= -180 && longitude <= 180, "longitude [-180, 180]")
  require(latitude >= -90 && latitude <= 90, "latitude [-90, 90]")
}
