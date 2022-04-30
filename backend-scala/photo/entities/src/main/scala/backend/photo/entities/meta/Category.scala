package backend.photo.entities.meta

sealed abstract class Category(val name: String)

object Category {
  case object Abstract extends Category("Abstract")
  case object Animal extends Category("Animal")
  case object CityAndArchitecture extends Category("City & Architecture")
  case object Landscape extends Category("Landscape")
  case object Nature extends Category("Nature")
  case object Night extends Category("Night")
  case object People extends Category("People")

  def apply(name: String): Category = name match {
    case "Abstract" => Abstract
    case "Animal" => Animal
    case "City & Architecture" => CityAndArchitecture
    case "Landscape" => Landscape
    case "Nature" => Nature
    case "Night" => Night
    case "People" => People
  }
}
