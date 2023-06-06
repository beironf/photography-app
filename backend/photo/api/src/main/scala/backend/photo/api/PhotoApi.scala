package backend.photo.api

import akka.http.scaladsl.model.StatusCodes.NoContent
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import backend.common.api.ApiApp
import backend.core.sqlstorage.DatabaseConnector

object PhotoApi extends App with ApiApp {

  private val apiRoutes = new ApiRoutes()

  val healthCheckRoute: Route = path("health-check") {
    // TODO: Check if API is ready, DB connection etc.
    get { complete(NoContent) }
  }

  private val route = {
    healthCheckRoute ~
      ApiDocs.route ~
      apiRoutes.route
  }

  start("photo-api", route, dbToValidate = Some(DatabaseConnector.MainDBConfig.db), shutdown = None)
}
