package backend.photo.api

import backend.common.api.ApiApp
import backend.core.sqlstorage.DatabaseConnector

object PhotoApi extends App with ApiApp {

  private val apiRoutes = new ApiRoutes()

  private val route = ApiDocs.route ~ apiRoutes.route

  start("photo-api", route, dbConnectorToValidate = Some(DatabaseConnector.defaultPostgresConnector), shutdown = None)
}
