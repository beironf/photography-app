package backend.photography.api

import backend.common.api.ApiApp
import backend.core.sqlstorage.DatabaseConnector
import backend.photography.repositories.*

object Api extends App with ApiApp {
  private val isProduction = config.getString("environment.name") == "production"

  private val photoRepository = PhotoRepositoryImpl()
  private val exifRepository = ImageExifRepositoryImpl()
  private val imageRepository = if (isProduction) {
    logger.info("Using production storage repository")
    ImageRepositoryFirebase()
  } else {
    logger.info("Using local storage repository")
    val localImageRepository = ImageRepositoryLocal()
    localImageRepository.necessaryPathsExist()
    localImageRepository
  }

  private val apiService = ApiService(photoRepository, imageRepository, exifRepository)

  private val apiRoutes = new ApiRoutes(apiService)

  private val route = ApiDocs.route ~ apiRoutes.route

  start("api", route, dbConnectorToValidate = Some(DatabaseConnector.defaultPostgresConnector), shutdown = None)
}
