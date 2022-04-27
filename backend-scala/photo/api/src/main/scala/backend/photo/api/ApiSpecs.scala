package backend.photo.api

import backend.common.api.model.ApiHttpErrorEndpoint.{EnvelopedHttpErrorEndpoint, toEnvelopedJson}
import backend.common.api.model.EndpointsSpec
import backend.common.api.utils.ApiHttpErrorsHandler.commonErrorsOut
import spray.json.DefaultJsonProtocol._
import sttp.tapir._


object ApiSpecs extends EndpointsSpec {

  private val photos = endpoint
    .in("v1")
    .in("photos")
    .tag("photos")

  val getPhotoEndpoint: EnvelopedHttpErrorEndpoint[String, Int] = // TODO: Add Photo output
    photos
      .get
      .in(path[String]("photoId"))
      .errorOut(commonErrorsOut)
      .out(toEnvelopedJson[Int]) // TODO: Add Photo output

  // Add the endpoint here in order to include it in the documentation
  val endpoints: List[AnyEndpoint] = getPhotoEndpoint :: Nil
}
