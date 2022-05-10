package backend.image.api

import akka.http.scaladsl.server.Route
import backend.common.api.ApiDocsSupport
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.circe.yaml._

object ApiDocs extends ApiDocsSupport {
  private val endpoints = ApiSpecs.endpoints
  private val openapi = OpenAPIDocsInterpreter().toOpenAPI(endpoints, "Image API", "v1")
  val route: Route = documentationRoute(openapi)

  /*def main(args: Array[String]): Unit = {
    import java.io._
    val pw = new PrintWriter("apidocs.yaml" )
    pw.write(openapi.toYaml)
    pw.close()
  }*/
}
