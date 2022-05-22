package backend.image.api

import backend.common.api.model.ApiHttpErrorEndpoint._
import backend.common.api.model.EndpointsSpec
import backend.common.api.utils.ApiHttpErrorsHandler.commonErrorsOut
import sttp.capabilities.akka.AkkaStreams
import sttp.model.{Part, StatusCode}
import sttp.tapir.generic.auto._
import sttp.tapir._

object ApiSpecs extends EndpointsSpec {

  case class ImageFileUpload(image: Part[TapirFile]) extends Serializable

  private val images = endpoint
    .in("v1")
    .in("images")
    .tag("images")

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

  // Add the endpoint here in order to include it in the documentation
  val endpoints: List[AnyEndpoint] = List(
    getImageEndpoint,
    uploadImageEndpoint,
    removeImageEndpoint
  )
}
