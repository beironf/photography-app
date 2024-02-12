package backend.photography.api

import backend.common.api.model.ApiHttpErrorEndpoint.*
import backend.common.api.model.{EndpointsSpec, Enveloped}
import backend.common.api.utils.ApiHttpErrorsHandler.commonErrorsOut
import backend.photography.api.model.*
import backend.photography.api.model.enums.CategoryDto
import sttp.capabilities.akka.AkkaStreams
import sttp.model.{Header, MediaType, Part, StatusCode}
import sttp.tapir.generic.auto.*
import sttp.tapir.*
import sttp.tapir.codec.enumeratum.TapirCodecEnumeratum
import sttp.tapir.json.spray.*

object ApiSpecs extends EndpointsSpec with JsonProtocol with TapirCodecEnumeratum {

  /**
   * ------- PHOTO -------
   */

  val validateAuthorizationTokenEndpoint: SecureHttpErrorEndpoint[Unit, Unit] = endpoint
    .in("v1")
    .in("auth")
    .tag("auth")
    .name("validateAuthorizationToken")
    .post
    .in("validate")
    .errorOut(commonErrorsOut)
    .out(statusCode(StatusCode.NoContent))
    .appendPasswordProtection

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
      .out(jsonBody[Enveloped[PhotoDto]])

  val listPhotosEndpoint: EnvelopedHttpErrorEndpoint[(Option[CategoryDto], Option[String], Option[Int], Option[Boolean]), Seq[PhotoWithRatioDto]] =
    photos
      .name("listPhotos")
      .get
      .in(query[Option[CategoryDto]]("category"))
      .in(query[Option[String]]("group"))
      .in(query[Option[Int]]("rating"))
      .in(query[Option[Boolean]]("inShowroom"))
      .errorOut(commonErrorsOut)
      .out(jsonBody[Enveloped[Seq[PhotoWithRatioDto]]])

  val listPhotoGroupsEndpoint: EnvelopedHttpErrorEndpoint[Unit, Seq[String]] =
    photoGroups
      .name("listPhotoGroups")
      .get
      .errorOut(commonErrorsOut)
      .out(jsonBody[Enveloped[Seq[String]]])

  val addPhotoEndpoint: SecureHttpErrorEndpoint[PhotoDto, Unit] =
    photos
      .name("addPhoto")
      .post
      .in(jsonBody[PhotoDto])
      .errorOut(commonErrorsOut)
      .out(statusCode(StatusCode.NoContent))
      .appendPasswordProtection

  val updatePhotoEndpoint: SecureHttpErrorEndpoint[(String, UpdatePhotoDto), Unit] =
    photos
      .name("updatePhoto")
      .post
      .in(path[String]("imageId"))
      .in(jsonBody[UpdatePhotoDto])
      .errorOut(commonErrorsOut)
      .out(statusCode(StatusCode.NoContent))
      .appendPasswordProtection

  /**
   * ------- IMAGES -------
   */

  case class ImageFileUpload(image: Part[TapirFile]) extends Serializable

  private val images = endpoint
    .in("v1")
    .in("images")
    .tag("images")

  private val thumbnails = endpoint
    .in("v1")
    .in("thumbnails")
    .tag("thumbnails")

  private val siteImages = endpoint
    .in("v1")
    .in("site-images")
    .tag("site-images")

  val listImagesEndpoint: EnvelopedHttpErrorEndpoint[Unit, Seq[ImageDto]] =
    images
      .name("listImages")
      .get
      .errorOut(commonErrorsOut)
      .out(jsonBody[Enveloped[Seq[ImageDto]]])

  val getImageEndpoint: HttpErrorStreamingEndpoint[String, AkkaStreams.BinaryStream, AkkaStreams] =
    images
      .name("getImage")
      .get
      .in(path[String]("imageId"))
      .errorOut(commonErrorsOut)
      .out(streamBody(AkkaStreams)(
        Schema(Schema.schemaForFile.schemaType),
        CodecFormat.OctetStream()
      ))
      .out(header(Header.contentType(MediaType.ImageJpeg)))

  val uploadImageEndpoint: SecureHttpErrorEndpoint[ImageFileUpload, Unit] =
    images
      .name("uploadImage")
      .post
      .in(multipartBody[ImageFileUpload](MultipartCodec.multipartCaseClassCodec[ImageFileUpload]))
      .errorOut(commonErrorsOut)
      .out(statusCode(StatusCode.NoContent))
      .appendPasswordProtection

  val removeImageEndpoint: SecureHttpErrorEndpoint[String, Unit] =
    images
      .name("removeImage")
      .description("Remove an image and its photo")
      .delete
      .in(path[String]("imageId"))
      .errorOut(commonErrorsOut)
      .out(statusCode(StatusCode.NoContent))
      .appendPasswordProtection

  val getThumbnailEndpoint: HttpErrorStreamingEndpoint[String, AkkaStreams.BinaryStream, AkkaStreams] =
    thumbnails
      .name("getThumbnail")
      .get
      .in(path[String]("imageId"))
      .errorOut(commonErrorsOut)
      .out(streamBody(AkkaStreams)(
        Schema(Schema.schemaForFile.schemaType),
        CodecFormat.OctetStream()
      ))
      .out(header(Header.contentType(MediaType.ImageJpeg)))

  val getSiteImageEndpoint: HttpErrorStreamingEndpoint[String, AkkaStreams.BinaryStream, AkkaStreams] =
    siteImages
      .name("getSiteImage")
      .get
      .in(path[String]("fileName"))
      .errorOut(commonErrorsOut)
      .out(streamBody(AkkaStreams)(
        Schema(Schema.schemaForFile.schemaType),
        CodecFormat.OctetStream()
      ))
      .out(header(Header.contentType(MediaType.ImagePng)))

  val getExifEndpoint: EnvelopedHttpErrorEndpoint[String, ImageExifDto] =
    images
      .name("getExif")
      .get
      .in(path[String]("imageId"))
      .in("exif")
      .errorOut(commonErrorsOut)
      .out(jsonBody[Enveloped[ImageExifDto]])

  // Add the endpoint here in order to include it in the documentation
  val endpoints: List[AnyEndpoint] = List(
    validateAuthorizationTokenEndpoint,
    getPhotoEndpoint,
    listPhotosEndpoint,
    listPhotoGroupsEndpoint,
    addPhotoEndpoint,
    updatePhotoEndpoint,
    listImagesEndpoint,
    getImageEndpoint,
    uploadImageEndpoint,
    removeImageEndpoint,
    getThumbnailEndpoint,
    getSiteImageEndpoint,
    getExifEndpoint
  )
}
