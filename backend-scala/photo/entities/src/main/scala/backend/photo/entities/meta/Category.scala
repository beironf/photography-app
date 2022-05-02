package backend.photo.entities.meta

sealed trait Category extends Enumeration

object Category extends Enumeration {
  type Category = Value
  val Abstract, Animal, CityAndArchitecture, Landscape, Nature, Night, People = Value
}
