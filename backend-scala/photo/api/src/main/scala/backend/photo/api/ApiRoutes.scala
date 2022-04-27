package backend.photo.api

import akka.http.scaladsl.server.Route
import backend.common.api.CommonApiRoute
import backend.photo.api.ApiSpecs._

import scala.concurrent.ExecutionContext

class ApiRoutes()(implicit executionContext: ExecutionContext) extends CommonApiRoute {

  private val apiService = new ApiService()

  private val getPhoto = tapirRoute(
    specification = getPhotoEndpoint,
    implementation = apiService.getPhoto
  )

  val route: Route =
    getPhoto
}
