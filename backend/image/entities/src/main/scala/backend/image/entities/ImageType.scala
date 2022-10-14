package backend.image.entities

object ImageType extends Enumeration {
  type ImageType = Value
  val FullSize, Thumbnail, Site = Value
}
