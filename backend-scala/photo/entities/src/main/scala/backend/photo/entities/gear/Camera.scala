package backend.photo.entities.gear

sealed abstract class Camera(val name: String)

object Camera {
  case object CanonEOS600D extends Camera("Canon EOS 600D")
  case object CanonEOS5DMarkII extends Camera("Canon EOS 5D Mark II")
  case object FujifilmX100F extends Camera("Fujifilm X100F")

  def apply(name: String): Camera = name match {
    case "Canon EOS 600D" => CanonEOS600D
    case "Canon EOS 5D Mark II" => CanonEOS5DMarkII
    case "Fujifilm X100F" => FujifilmX100F
  }
}
