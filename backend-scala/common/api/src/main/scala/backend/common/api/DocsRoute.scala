package backend.common.api

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import backend.common.api.utils.CustomMediaTypes
import io.circe.syntax.EncoderOps
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.openapi.circe._
import sttp.tapir.openapi.circe.yaml.RichOpenAPI

object DocsRoute {

  def openApiRoute(openApi: OpenAPI): Route = (pathPrefix("api-docs") & get) {
    path("swagger.yaml" | "openapi.yaml") {
      complete(HttpEntity(CustomMediaTypes.`text/vnd.yaml`, openApi.toYaml))
    } ~
      path("swagger.json" | "openapi.json") {
        complete(HttpEntity(MediaTypes.`application/json`, openApi.asJson.toString()))
      }
  }

}
