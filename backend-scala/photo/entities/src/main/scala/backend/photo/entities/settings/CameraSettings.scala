package backend.photo.entities.settings

case class CameraSettings(focalLength: Int,
                          fNumber: Float,
                          iso: Int,
                          exposureTime: String)
