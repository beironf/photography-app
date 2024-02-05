package backend.photography.entities.photo.meta

object CameraTechnique extends Enumeration {
  type CameraTechnique = Value
  val LongExposure, Panorama, Aerial, Macro, Zooming, Filters, MultipleFocusPoints = Value
}
