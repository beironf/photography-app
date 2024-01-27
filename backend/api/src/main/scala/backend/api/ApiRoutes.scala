package backend.api

import cats.implicits.catsSyntaxEitherId
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import backend.common.api.CommonApiRoute
import backend.common.api.model.ApiHttpErrors.HttpError
import backend.core.application.DefaultService
import ApiSpecs.*

import scala.concurrent.{ExecutionContext, Future}

class ApiRoutes(apiService: ApiService)
               (implicit executionContext: ExecutionContext) extends CommonApiRoute with DefaultService {

  private val validateAuthorizationToken = secureEndpoint(
    specification = validateAuthorizationTokenEndpoint,
    implementation = (_: Unit) => Future {
      ((): Unit).asRight[HttpError]
    }
  )

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
    validateAuthorizationToken,
    getPhoto,
    listPhotos,
    listPhotoGroups,
    addPhoto,
    updatePhoto,
    removePhoto,
    listImages,
    getImage,
    uploadImage,
    removeImage,
    getThumbnail,
    getSiteImage,
    getExif
  )
}
