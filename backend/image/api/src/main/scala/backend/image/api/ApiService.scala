package backend.image.api

import backend.common.api.model.ApiHttpErrors._
import backend.common.api.model.ApiHttpResponse._
import backend.common.api.utils.ApiServiceSupport
import backend.image.api.ApiSpecs.ImageFileUpload
import backend.image.api.model.{ImageExifDto, ImplicitDtoConversion}
import backend.image.entities.{ImageExif, ImageIO}
import backend.image.interactors.{ImageExifService, ImageService}
import com.sksamuel.scrimage.metadata.ImageMetadata
import sttp.capabilities.akka.AkkaStreams

import scala.concurrent.{ExecutionContext, Future}

class ApiService(service: ImageService,
                 exifService: ImageExifService)
                (implicit executionContext: ExecutionContext) extends ApiServiceSupport
  with ImageIO
  with ImplicitDtoConversion {

  private val MAX_IMAGE_SIZE = 4000
  private val MAX_THUMBNAIL_SIZE = 1200

  def getImage(imageId: String): Future[HttpResponse[AkkaStreams.BinaryStream]] =
    service.getImageStream(imageId).map {
      case Some(stream) => Right(stream)
      case None => Left(NotFound(s"[imageId: $imageId] Image not found"))
    }

  def getThumbnail(imageId: String): Future[HttpResponse[AkkaStreams.BinaryStream]] =
    service.getThumbnailStream(imageId).map {
      case Some(stream) => Right(stream)
      case None => Left(NotFound(s"[imageId: $imageId] Thumbnail not found"))
    }

  def getImageIds: Future[EnvelopedHttpResponse[Seq[String]]] =
    service.getImageNames
      .toEnvelopedHttpResponse

  def getImageExif(imageId: String): Future[EnvelopedHttpResponse[ImageExifDto]] =
    exifService.getExif(imageId).map {
      case Some(exif) => Right(exif.toDto).asInstanceOf[Right[HttpError, ImageExifDto]]
      case None => Left(NotFound(s"[imageId: $imageId] Metadata not found"))
    }.toEnveloped

  def uploadImage(form: ImageFileUpload): Future[HttpResponse[Unit]] = {
    form.image.fileName.map { fileName =>
      if (fileName.contains(".jpg")) {
        val exif = ExifUtil.getExif(form.image.body)

        val resizedImage = ImageResizer.resizeImage(form.image.body, MAX_IMAGE_SIZE)
        val thumbnail = ImageResizer.resizeImage(form.image.body, MAX_THUMBNAIL_SIZE)

        (for {
          _ <- exifService.addExif(fileName, exif)
          _ <- service.uploadImage(fileName, resizedImage)
          _ <- service.uploadThumbnail(fileName, thumbnail)
        } yield (): Unit)
          .toHttpResponse
      } else Future.successful(Left(BadRequest("The only image format supported is .jpg")))
    }.getOrElse(Future.successful(Left(BadRequest("File name not found"))))
  }

  def removeImage(imageId: String): Future[HttpResponse[Unit]] = {
    (for {
      _ <- service.removeThumbnail(imageId)
      _ <- service.removeImage(imageId)
    } yield (): Unit).toHttpResponse
  }

}
