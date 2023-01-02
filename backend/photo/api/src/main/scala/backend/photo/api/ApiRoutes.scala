package backend.photo.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import backend.common.api.CommonApiRoute
import backend.exif.adapters.ImageExifRepositoryImpl
import backend.exif.interactors.ImageExifService
import backend.photo.adapters.PhotoRepositoryImpl
import backend.photo.api.ApiSpecs._
import backend.photo.interactors.PhotoService

import scala.concurrent.ExecutionContext

class ApiRoutes()(implicit executionContext: ExecutionContext) extends CommonApiRoute {

  private val photoRepository = PhotoRepositoryImpl()
  private val photoService = new PhotoService(photoRepository)
  private val validationService = new ApiValidationService(photoRepository)
  private val exifRepository = ImageExifRepositoryImpl()
  private val exifService = new ImageExifService(exifRepository)
  private val apiService = new ApiService(photoService, validationService, exifService)

  private val getPhoto = endpoint(
    specification = getPhotoEndpoint,
    implementation = apiService.getPhoto
  )

  private val listPhotos = endpoint(
    specification = listPhotosEndpoint,
    implementation = (apiService.listPhotos _).tupled
  )

  private val listPhotoGroups = endpoint(
    specification = listPhotoGroupsEndpoint,
    implementation = (_: Unit) => apiService.listPhotoGroups
  )

  private val addPhoto = secureEndpoint(
    specification = addPhotoEndpoint,
    implementation = apiService.addPhoto
  )

  private val updatePhoto = secureEndpoint(
    specification = updatePhotoEndpoint,
    implementation = (apiService.updatePhoto _).tupled
  )

  private val removePhoto = secureEndpoint(
    specification = removePhotoEndpoint,
    implementation = apiService.removePhoto
  )

  val route: Route =
    getPhoto ~
      listPhotos ~
      listPhotoGroups ~
      addPhoto ~
      updatePhoto ~
      removePhoto
}
