package backend.common.api.utils

import akka.http.scaladsl.model.HttpCharsets.`UTF-8`
import akka.http.scaladsl.model.MediaType

object CustomMediaTypes {
  /**
   * [[http://media-types.ietf.narkive.com/emZX0ly2/proposed-media-type-registration-for-yaml]].
   */
  val `text/vnd.yaml`: MediaType.WithFixedCharset =
    MediaType.customWithFixedCharset("text", "vnd.yaml", `UTF-8`)
}
