package backend.image.api

import backend.common.api.model.ApiHttpErrors._
import backend.common.api.model.ApiHttpResponse._
import backend.common.api.utils.ApiServiceSupport
import backend.image.api.ApiSpecs.ImageFileUpload
import backend.image.entities.ImageIO
import backend.image.interactors.ImageService
import sttp.capabilities.akka.AkkaStreams

import java.nio.file.Files
import scala.concurrent.{ExecutionContext, Future}

class ApiService(service: ImageService)
                (implicit executionContext: ExecutionContext) extends ApiServiceSupport with ImageIO {

  private val MAX_THUMBNAIL_SIZE = 600

  def getImage(imageId: String): Future[HttpResponse[AkkaStreams.BinaryStream]] =
    service.getImageStream(imageId).map {
      case Some(stream) => Right(stream)
      case None => Left(NotFound(s"[imageId: $imageId] Not found"))
    }

  def uploadImage(form: ImageFileUpload): Future[HttpResponse[Unit]] = {
    form.image.fileName.map { fileName =>
      if (fileName.contains(".jpg")) {
        val thumbnail = ImageResizer.resizeImage(form.image.body, MAX_THUMBNAIL_SIZE)
        (for {
          _ <- service.uploadImage(fileName, Files.readAllBytes(form.image.body.toPath))
          _ <- service.uploadThumbnail(fileName, thumbnail)
        } yield (): Unit)
          .toHttpResponse
      } else Future.successful(Left(BadRequest("The only image format supported is .jpg")))
    }.getOrElse(Future.successful(Left(BadRequest("File name not found"))))
  }

  def removeImage(imageId: String): Future[HttpResponse[Unit]] =
    service.removeImage(imageId).toHttpResponse

}
