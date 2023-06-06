package backend.image.api

import akka.http.scaladsl.model.StatusCodes.NoContent
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import backend.common.api.ApiApp
import backend.core.sqlstorage.DatabaseConnector

object ImageApi extends App with ApiApp {

  private val apiRoutes = new ApiRoutes()

  val healthCheckRoute: Route = path("health-check") {
    get { complete(NoContent) }
  }

  private val route = {
    healthCheckRoute ~
      ApiDocs.route ~
      apiRoutes.route
  }

  start("image-api", route, dbToValidate = Some(DatabaseConnector.MainDBConfig.db), shutdown = None)
}
