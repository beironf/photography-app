package backend.image.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import backend.common.api.CommonApiRoute
import backend.image.adapters.{ImageExifRepositoryImpl, ImageRepositoryImpl}
import backend.image.api.ApiSpecs._
import backend.image.interactors.{ImageExifService, ImageService}

import scala.concurrent.ExecutionContext

class ApiRoutes()(implicit executionContext: ExecutionContext) extends CommonApiRoute {

  private val imageRepository = ImageRepositoryImpl()
  private val imageExifRepository = ImageExifRepositoryImpl()
  private val imageService = new ImageService(imageRepository)
  private val imageExifService = new ImageExifService(imageExifRepository)
  private val apiServiceValidator = new ApiServiceValidator(imageService)
  private val apiService = new ApiService(imageService, imageExifService, apiServiceValidator)

  private val listImages = endpoint(
    specification = listImagesEndpoint,
    implementation = (_: Unit) => apiService.listImages
  )

  private val getImage = streamingEndpoint(
    specification = getImageEndpoint,
    implementation = apiService.getImage
  )

  private val getThumbnail = streamingEndpoint(
    specification = getThumbnailEndpoint,
    implementation = apiService.getThumbnail
  )

  private val uploadImage = endpoint(
    specification = uploadImageEndpoint,
    implementation = apiService.uploadImage
  )

  private val removeImage = endpoint(
    specification = removeImageEndpoint,
    implementation = apiService.removeImage
  )

  private val getExif = endpoint(
    specification = getExifEndpoint,
    implementation = apiService.getImageExif
  )

  val route: Route = {
    listImages ~
      getImage ~
      uploadImage ~
      removeImage ~
      getThumbnail ~
      getExif
  }
}
