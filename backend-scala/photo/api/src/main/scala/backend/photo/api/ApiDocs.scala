package backend.photo.api

import akka.http.scaladsl.server.Route
import backend.common.api.ApiDocsSupport
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.circe.yaml._

object ApiDocs {
  private val endpoints = ApiSpecs.endpoints
  private val openapi = OpenAPIDocsInterpreter().toOpenAPI(endpoints, "Photo API", "v1")
  val documentationRoute: Route = ApiDocsSupport.documentationRoute(openapi)

  /*def main(args: Array[String]): Unit = {
    import java.io._
    val pw = new PrintWriter("apidocs.yaml" )
    pw.write(openapi.toYaml)
    pw.close()
  }*/
}
