package backend.photo.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import backend.common.api.CommonApiRoute
import backend.photo.adapters.PhotoRepositoryImpl
import backend.photo.api.ApiSpecs._
import backend.photo.interactors.PhotoService

import scala.concurrent.ExecutionContext

class ApiRoutes()(implicit executionContext: ExecutionContext) extends CommonApiRoute {

  private val photoRepository = PhotoRepositoryImpl()
  private val photoService = new PhotoService(photoRepository)
  private val validationService = new ApiValidationService(photoRepository)
  private val apiService = new ApiService(photoService, validationService)

  private val getPhoto = tapirRoute(
    specification = getPhotoEndpoint,
    implementation = apiService.getPhoto
  )

  private val listPhotos = tapirRoute(
    specification = listPhotosEndpoint,
    implementation = (apiService.listPhotos _).tupled
  )

  private val addPhotos = tapirRoute(
    specification = addPhotoEndpoint,
    implementation = apiService.addPhoto
  )

  private val removePhoto = tapirRoute(
    specification = removePhotoEndpoint,
    implementation = apiService.removePhoto
  )

  val route: Route =
    getPhoto ~
      listPhotos ~
      addPhotos ~
      removePhoto
}
