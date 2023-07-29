package backend.image.api

import backend.common.api.ApiApp
import backend.core.sqlstorage.DatabaseConnector

object ImageApi extends App with ApiApp {

  private val apiRoutes = new ApiRoutes()

  private val route = ApiDocs.route ~ apiRoutes.route

  start("image-api", route, dbConfigToValidate = Some(DatabaseConnector.MainDBConfig), shutdown = None)
}
