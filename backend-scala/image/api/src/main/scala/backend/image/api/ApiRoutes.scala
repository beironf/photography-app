package backend.image.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import backend.common.api.CommonApiRoute
import backend.image.adapters.ImageRepositoryImpl
import backend.image.api.ApiSpecs._
import backend.image.interactors.ImageService

import scala.concurrent.ExecutionContext

class ApiRoutes()(implicit executionContext: ExecutionContext) extends CommonApiRoute {

  private val imageRepository = ImageRepositoryImpl()
  private val imageService = new ImageService(imageRepository)
  private val apiService = new ApiService(imageService)

  private val getImage = streamingTapirRoute(
    specification = getImageEndpoint,
    implementation = apiService.getImage
  )

  private val uploadImage = tapirRoute(
    specification = uploadImageEndpoint,
    implementation = apiService.uploadImage
  )

  private val removeImage = tapirRoute(
    specification = removeImageEndpoint,
    implementation = apiService.removeImage
  )

  val route: Route =
    getImage ~
      uploadImage ~
      removeImage
}
