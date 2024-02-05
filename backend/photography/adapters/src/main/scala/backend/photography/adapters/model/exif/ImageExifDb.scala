package backend.photography.adapters.model.exif

import backend.photography.entities.exif.ImageExif

import java.sql.Timestamp

case class ImageExifDb(imageId: String,
                       cameraMake: Option[String],
                       cameraModel: Option[String],
                       lens: Option[String],
                       focalLength: Option[Float],
                       fNumber: Option[Float],
                       iso: Option[Int],
                       exposureTime: Option[String],
                       date: Option[Timestamp],
                       width: Int,
                       height: Int) {
  def toDomain: ImageExif =
    ImageExif(cameraMake, cameraModel, lens, focalLength, fNumber, iso, exposureTime, date.map(_.toInstant), width, height)
}
