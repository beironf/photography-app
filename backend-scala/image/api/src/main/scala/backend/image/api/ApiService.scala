package backend.image.api

import backend.common.api.model.ApiHttpResponse._
import backend.common.api.utils.ApiExceptionsHandler
import backend.common.model.CommonExceptions._
import backend.image.entities.ImageIO
import backend.image.interactors.ImageService
import sttp.capabilities.akka.AkkaStreams
import sttp.tapir.TapirFile

import scala.concurrent.{ExecutionContext, Future}

class ApiService(service: ImageService)
                (implicit executionContext: ExecutionContext) extends ApiExceptionsHandler with ImageIO {

  @throws[NotFoundException]
  def getImage(imageId: String): Future[HttpResponse[AkkaStreams.BinaryStream]] =
    service.getImageStream(imageId).map(_
      .getOrElse(throw NotFoundException(s"[imageId: $imageId] Not found")))
      .toHttpResponse

  @throws[BadRequestException]
  def uploadImage(file: TapirFile): Future[HttpResponse[Unit]] = {
    if (file.getName.contains(".jpg"))
      service.uploadImageStream(file).toHttpResponse
    else Future.failed(BadRequestException("The only image format supported is .jpg"))
  }

  def removeImage(imageId: String): Future[HttpResponse[Unit]] =
    service.removeImage(imageId).toHttpResponse

}
