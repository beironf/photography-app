package backend.image.api

import backend.common.api.model.ApiHttpErrors._
import backend.common.api.model.ApiHttpResponse._
import backend.common.api.utils.ApiServiceSupport
import backend.image.entities.ImageIO
import backend.image.interactors.ImageService
import sttp.capabilities.akka.AkkaStreams
import sttp.tapir.TapirFile

import scala.concurrent.{ExecutionContext, Future}

class ApiService(service: ImageService)
                (implicit executionContext: ExecutionContext) extends ApiServiceSupport with ImageIO {

  def getImage(imageId: String): Future[HttpResponse[AkkaStreams.BinaryStream]] =
    service.getImageStream(imageId).map {
      case Some(stream) => Right(stream)
      case None => Left(NotFound(s"[imageId: $imageId] Not found"))
    }

  def uploadImage(file: TapirFile): Future[HttpResponse[Unit]] = {
    if (file.getName.contains(".jpg"))
      service.uploadImageStream(file).toHttpResponse
    else Future.successful(Left(BadRequest("The only image format supported is .jpg")))
  }

  def removeImage(imageId: String): Future[HttpResponse[Unit]] =
    service.removeImage(imageId).toHttpResponse

}
