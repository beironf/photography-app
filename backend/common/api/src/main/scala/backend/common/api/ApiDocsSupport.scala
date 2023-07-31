package backend.common.api

import akka.http.scaladsl.model.*
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import backend.common.api.utils.CustomMediaTypes
import io.circe.syntax.EncoderOps
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.openapi.circe.*
import sttp.tapir.openapi.circe.yaml.RichOpenAPI

trait ApiDocsSupport {
  def documentationRoute(openApi: OpenAPI): Route =
    path("api") {
      getFromResource("redoc/index.html")
    } ~
    //this requires a prefix test to hide everything that the doc services creates
    pathPrefixTest("api-docs") {
      openApiRoute(openApi)
    }

  private def openApiRoute(openApi: OpenAPI): Route = (pathPrefix("api-docs") & get) {
    path("swagger.yaml" | "openapi.yaml") {
      complete(HttpEntity(CustomMediaTypes.`text/vnd.yaml`, openApi.toYaml))
    } ~
      path("swagger.json" | "openapi.json") {
        complete(HttpEntity(MediaTypes.`application/json`, openApi.asJson.toString()))
      }
  }
}
