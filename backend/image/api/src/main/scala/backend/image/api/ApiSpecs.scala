package backend.image.api

import backend.common.api.model.ApiHttpErrorEndpoint._
import backend.common.api.model.EndpointsSpec
import backend.common.api.utils.ApiHttpErrorsHandler.commonErrorsOut
import backend.image.api.model.{ImageDto, ImageExifDto, JsonProtocol}
import sttp.capabilities.akka.AkkaStreams
import sttp.model.{Part, StatusCode}
import sttp.tapir.generic.auto._
import sttp.tapir._

object ApiSpecs extends EndpointsSpec with JsonProtocol {

  case class ImageFileUpload(image: Part[TapirFile]) extends Serializable

  private val images = endpoint
    .in("v1")
    .in("images")
    .tag("images")

  private val thumbnails = endpoint
    .in("v1")
    .in("thumbnails")
    .tag("thumbnails")

  val listImagesEndpoint: EnvelopedHttpErrorEndpoint[Unit, Seq[ImageDto]] =
    images
      .name("listImages")
      .get
      .errorOut(commonErrorsOut)
      .out(toEnvelopedJson[Seq[ImageDto]])

  val getImageEndpoint: AkkaStreamsEndpoint[String, AkkaStreams.BinaryStream] =
    images
      .name("getImage")
      .get
      .in(path[String]("imageId"))
      .errorOut(commonErrorsOut)
      .out(streamBody(AkkaStreams)(
        Schema(Schema.schemaForFile.schemaType),
        CodecFormat.OctetStream()
      ))

  val uploadImageEndpoint: HttpErrorEndpoint[ImageFileUpload, Unit] =
    images
      .name("uploadImage")
      .post
      .in(multipartBody[ImageFileUpload](MultipartCodec.multipartCaseClassCodec[ImageFileUpload]))
      .errorOut(commonErrorsOut)
      .out(statusCode(StatusCode.NoContent))

  val removeImageEndpoint: HttpErrorEndpoint[String, Unit] =
    images
      .name("removeImage")
      .delete
      .in(path[String]("imageId"))
      .errorOut(commonErrorsOut)
      .out(statusCode(StatusCode.NoContent))

  val getThumbnailEndpoint: AkkaStreamsEndpoint[String, AkkaStreams.BinaryStream] =
    thumbnails
      .name("getThumbnail")
      .get
      .in(path[String]("imageId"))
      .errorOut(commonErrorsOut)
      .out(streamBody(AkkaStreams)(
        Schema(Schema.schemaForFile.schemaType),
        CodecFormat.OctetStream()
      ))

  val getExifEndpoint: EnvelopedHttpErrorEndpoint[String, ImageExifDto] =
    images
      .name("getExif")
      .get
      .in(path[String]("imageId"))
      .in("exif")
      .errorOut(commonErrorsOut)
      .out(toEnvelopedJson[ImageExifDto])

  // Add the endpoint here in order to include it in the documentation
  val endpoints: List[AnyEndpoint] = List(
    listImagesEndpoint,
    getImageEndpoint,
    uploadImageEndpoint,
    removeImageEndpoint,
    getThumbnailEndpoint,
    getExifEndpoint
  )
}
