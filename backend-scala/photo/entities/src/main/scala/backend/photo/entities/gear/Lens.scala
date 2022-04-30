package backend.photo.entities.gear

sealed abstract class Lens(val name: String)

object Lens {
  case object Sigma10_20 extends Lens("Sigma 10-20mm f/4-5.6 EX DC HSM")
  case object Canon50F1_8 extends Lens("Canon 50mm f:1.8")
  case object Canon70_300 extends Lens("Canon 70-300mm f:4-5.6")
  case object Canon24_70L extends Lens("Canon 24-70mm 2.8 L")
  case object Fujinon35 extends Lens("Fujinon 35mm f:2")

  def apply(name: String): Lens = name match {
    case "Sigma 10-20mm f/4-5.6 EX DC HSM" => Sigma10_20
    case "Canon 50mm f:1.8" => Canon50F1_8
    case "Canon 70-300mm f:4-5.6" => Canon70_300
    case "Canon 24-70mm 2.8 L" => Canon24_70L
    case "Fujinon 35mm f:2" => Fujinon35
  }
}
