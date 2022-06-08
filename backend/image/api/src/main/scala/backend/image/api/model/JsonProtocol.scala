package backend.image.api.model

import backend.common.json.JsonSupport
import spray.json._

trait JsonProtocol extends JsonSupport {
  implicit val exifFormat: RootJsonFormat[ImageExifDto] = jsonFormat10(ImageExifDto)
}
