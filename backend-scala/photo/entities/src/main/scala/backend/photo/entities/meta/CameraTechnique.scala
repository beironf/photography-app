package backend.photo.entities.meta

object CameraTechnique extends Enumeration {
  type CameraTechnique = Value
  val LongExposure, Panorama, Aerial, Macro, Zooming, Filters, MultipleFocusPoints = Value
}
