package backend.photo.api

import akka.http.scaladsl.model.StatusCodes.NoContent
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import backend.common.api.ApiApp

object PhotoApi extends App with ApiApp {

  private val apiRoutes = new ApiRoutes()

  val healthCheckRoute: Route = path("health-check") {
    // TODO: Check if API is ready, DB connection etc.
    get { complete(NoContent) }
  }

  private val route = {
    healthCheckRoute ~
      ApiDocs.documentationRoute ~
      apiRoutes.route
  }

  start("photo-api", route, shutdown = None)
}
