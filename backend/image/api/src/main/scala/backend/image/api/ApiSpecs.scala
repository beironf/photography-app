package backend.image.api

import backend.common.api.model.ApiHttpErrorEndpoint._
import backend.common.api.model.EndpointsSpec
import backend.common.api.utils.ApiHttpErrorsHandler.commonErrorsOut
import backend.image.api.model.{ImageExifDto, JsonProtocol}
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

  val getImageIdsEndpoint: EnvelopedHttpErrorEndpoint[Unit, Seq[String]] =
    images
      .name("getImageIds")
      .get
      .errorOut(commonErrorsOut)
      .out(toEnvelopedJson[Seq[String]])

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
    getImageIdsEndpoint,
    getImageEndpoint,
    uploadImageEndpoint,
    removeImageEndpoint,
    getExifEndpoint
  )
}
