package backend.photo.api

import backend.common.api.model.ApiHttpErrorEndpoint._
import backend.common.api.model.EndpointsSpec
import backend.common.api.utils.ApiHttpErrorsHandler.commonErrorsOut
import backend.photo.api.model._
import backend.photo.api.model.dtos._
import backend.photo.api.model.enums.CategoryDto
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.codec.enumeratum.TapirCodecEnumeratum
import sttp.tapir.generic.auto._
import sttp.tapir.json.spray.jsonBody


object ApiSpecs extends EndpointsSpec
  with JsonProtocol
  with TapirCodecEnumeratum {

  private val photos = endpoint
    .in("v1")
    .in("photos")
    .tag("photos")

  private val photoGroups = endpoint
    .in("v1")
    .in("photo-groups")
    .tag("photo-groups")

  val getPhotoEndpoint: EnvelopedHttpErrorEndpoint[String, PhotoDto] =
    photos
      .name("getPhoto")
      .get
      .in(path[String]("imageId"))
      .errorOut(commonErrorsOut)
      .out(toEnvelopedJson[PhotoDto])

  val listPhotosEndpoint: EnvelopedHttpErrorEndpoint[(Option[CategoryDto], Option[String], Option[Int], Option[Boolean]), Seq[PhotoWithRatioDto]] =
    photos
      .name("listPhotos")
      .get
      .in(query[Option[CategoryDto]]("category"))
      .in(query[Option[String]]("group"))
      .in(query[Option[Int]]("rating"))
      .in(query[Option[Boolean]]("inShowroom"))
      .errorOut(commonErrorsOut)
      .out(toEnvelopedJson[Seq[PhotoWithRatioDto]])

  val listPhotoGroupsEndpoint: EnvelopedHttpErrorEndpoint[Unit, Seq[String]] =
    photoGroups
      .name("listPhotoGroups")
      .get
      .errorOut(commonErrorsOut)
      .out(toEnvelopedJson[Seq[String]])

  val addPhotoEndpoint: HttpErrorEndpoint[PhotoDto, Unit] =
    photos
      .name("addPhoto")
      .post
      .in(jsonBody[PhotoDto])
      .errorOut(commonErrorsOut)
      .out(statusCode(StatusCode.NoContent))

  val updatePhotoEndpoint: HttpErrorEndpoint[(String, UpdatePhotoDto), Unit] =
    photos
      .name("updatePhoto")
      .post
      .in(path[String]("imageId"))
      .in(jsonBody[UpdatePhotoDto])
      .errorOut(commonErrorsOut)
      .out(statusCode(StatusCode.NoContent))

  val removePhotoEndpoint: HttpErrorEndpoint[String, Unit] =
    photos
      .name("removePhoto")
      .delete
      .in(path[String]("imageId"))
      .errorOut(commonErrorsOut)
      .out(statusCode(StatusCode.NoContent))

  // Add the endpoint here in order to include it in the documentation
  val endpoints: List[AnyEndpoint] = List(
    getPhotoEndpoint,
    listPhotosEndpoint,
    listPhotoGroupsEndpoint,
    addPhotoEndpoint,
    updatePhotoEndpoint,
    removePhotoEndpoint
  )
}
