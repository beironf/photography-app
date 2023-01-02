package backend.exif.entities

import java.time.Instant

case class ImageExif(cameraMake: Option[String],
                     cameraModel: Option[String],
                     lens: Option[String],
                     focalLength: Option[Float],
                     fNumber: Option[Float],
                     iso: Option[Int],
                     exposureTime: Option[String],
                     date: Option[Instant],
                     width: Int,
                     height: Int)
