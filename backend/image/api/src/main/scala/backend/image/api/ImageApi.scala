package backend.image.api

import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import backend.common.api.ApiApp
import backend.core.sqlstorage.DatabaseConnector

object ImageApi extends App with ApiApp {

  private val apiRoutes = new ApiRoutes()

  val healthCheckRoute: Route = path("health-check") {
    get { complete(OK) }
  }

  private val route = {
    healthCheckRoute ~
      ApiDocs.route ~
      apiRoutes.route
  }

  start("image-api", route, dbConfigToValidate = Some(DatabaseConnector.MainDBConfig), shutdown = None)
}
