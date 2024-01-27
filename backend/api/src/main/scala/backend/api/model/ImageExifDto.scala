package backend.api.model

import java.time.Instant

case class ImageExifDto(cameraMake: Option[String],
                        cameraModel: Option[String],
                        lens: Option[String],
                        focalLength: Option[Float],
                        fNumber: Option[Float],
                        iso: Option[Int],
                        exposureTime: Option[String],
                        date: Option[Instant],
                        width: Int,
                        height: Int)
