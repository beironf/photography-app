package backend.image.adapters.exifdb

import backend.image.entities.ImageExif

import java.time.Instant

case class ImageExifDb(imageId: String,
                       cameraMake: Option[String],
                       cameraModel: Option[String],
                       lens: Option[String],
                       focalLength: Option[Int],
                       fNumber: Option[Float],
                       iso: Option[Int],
                       exposureTime: Option[String],
                       date: Option[Instant]) {
  def toDomain: ImageExif = ImageExif(cameraMake, cameraModel, lens, focalLength, fNumber, iso, exposureTime, date)
}
