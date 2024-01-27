package backend.api

import akka.http.scaladsl.server.Route
import backend.common.api.ApiDocsSupport
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter

object ApiDocs extends ApiDocsSupport {
  private val endpoints = ApiSpecs.endpoints
  private val openapi = OpenAPIDocsInterpreter().toOpenAPI(endpoints, "Photography API", "v1")
  val route: Route = documentationRoute(openapi)

  /*def main(args: Array[String]): Unit = {
    import java.io._
    val pw = new PrintWriter("apidocs.yaml" )
    pw.write(openapi.toYaml)
    pw.close()
  }*/
}
