package backend.photo.entities.meta

sealed abstract class CameraTechnique(val name: String)

object CameraTechnique {
  case object LongExposure extends CameraTechnique("Long Exposure")
  case object Panorama extends CameraTechnique("Panorama")
  case object Aerial extends CameraTechnique("Aerial")
  case object Macro extends CameraTechnique("Macro")
  case object Zooming extends CameraTechnique("Zooming")
  case object Filters extends CameraTechnique("Filters")
  case object MultipleFocusPoints extends CameraTechnique("Multiple Focus Points")

  def apply(name: String): CameraTechnique = name match {
    case "Long Exposure" => LongExposure
    case "Panorama" => Panorama
    case "Aerial" => Aerial
    case "Macro" => Macro
    case "Zooming" => Zooming
    case "Filters" => Filters
    case "Multiple Focus Points" => MultipleFocusPoints
  }
}
