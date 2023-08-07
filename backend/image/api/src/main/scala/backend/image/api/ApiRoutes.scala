package backend.image.api

import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import backend.common.api.CommonApiRoute
import backend.core.application.DefaultService
import backend.exif.adapters.ImageExifRepositoryImpl
import backend.exif.interactors.ImageExifService
import backend.image.adapters.{ImageRepositoryGCS, ImageRepositoryImpl}
import backend.image.api.ApiSpecs.*
import backend.image.interactors.ImageService

import scala.concurrent.ExecutionContext

class ApiRoutes()(implicit executionContext: ExecutionContext) extends CommonApiRoute with DefaultService {

  logger.info(s"-------> environment: ${config.getString("environment.name")}")
  private val isProduction = config.getString("environment.name") == "production"
  private val imageRepository = if (isProduction) {
    logger.info("Using production storage repository")
    ImageRepositoryGCS()
  } else {
    logger.info("Using local storage repository")
    val localImageRepository = ImageRepositoryImpl()
    localImageRepository.necessaryPathsExist()
    localImageRepository
  }
  private val imageExifRepository = ImageExifRepositoryImpl()
  private val imageService = new ImageService(imageRepository)
  private val imageExifService = new ImageExifService(imageExifRepository)
  private val apiServiceValidator = new ApiServiceValidator(imageService)
  private val apiService = new ApiService(imageService, imageExifService, apiServiceValidator)

  private val listImages = endpoint(
    specification = listImagesEndpoint,
    implementation = (_: Unit) => apiService.listImages
  )

  private val getImage = streamingAkkaEndpoint(
    specification = getImageEndpoint,
    implementation = apiService.getImage
  )

  private val getThumbnail = streamingAkkaEndpoint(
    specification = getThumbnailEndpoint,
    implementation = apiService.getThumbnail
  )

  private val getSiteImage = streamingAkkaEndpoint(
    specification = getSiteImageEndpoint,
    implementation = apiService.getSiteImage
  )

  private val uploadImage = secureEndpoint(
    specification = uploadImageEndpoint,
    implementation = apiService.uploadImage
  )

  private val removeImage = secureEndpoint(
    specification = removeImageEndpoint,
    implementation = apiService.removeImage
  )

  private val getExif = endpoint(
    specification = getExifEndpoint,
    implementation = apiService.getImageExif
  )

  val route: Route = concat(
    listImages,
    getImage,
    uploadImage,
    removeImage,
    getThumbnail,
    getSiteImage,
    getExif
  )
}
