package backend.api

import backend.common.api.ApiApp
import backend.core.sqlstorage.DatabaseConnector
import backend.exif.adapters.ImageExifRepositoryImpl
import backend.exif.interactors.ImageExifService
import backend.image.adapters.{ImageRepositoryGCS, ImageRepositoryImpl}
import backend.image.interactors.ImageService
import backend.photo.adapters.PhotoRepositoryImpl
import backend.photo.interactors.PhotoService

object Api extends App with ApiApp {

  private val isProduction = config.getString("environment.name") == "production"

  private val photoRepository = PhotoRepositoryImpl()
  private val exifRepository = ImageExifRepositoryImpl()
  private val imageRepository = if (isProduction) {
    logger.info("Using production storage repository")
    ImageRepositoryGCS()
  } else {
    logger.info("Using local storage repository")
    val localImageRepository = ImageRepositoryImpl()
    localImageRepository.necessaryPathsExist()
    localImageRepository
  }

  private val photoService = new PhotoService(photoRepository)
  private val imageService = new ImageService(imageRepository)
  private val exifService = new ImageExifService(exifRepository)
  private val apiServiceValidator = new ApiServiceValidator(photoRepository, imageRepository)

  private val apiService = new ApiService(photoService, imageService, exifService, apiServiceValidator)

  private val apiRoutes = new ApiRoutes(apiService)

  private val route = ApiDocs.route ~ apiRoutes.route

  start("api", route, dbConnectorToValidate = Some(DatabaseConnector.defaultPostgresConnector), shutdown = None)
}
