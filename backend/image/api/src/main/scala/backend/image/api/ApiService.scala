package backend.image.api

import backend.common.api.model.ApiHttpErrors._
import backend.common.api.model.ApiHttpResponse._
import backend.common.api.utils.ApiServiceSupport
import backend.image.api.ApiSpecs.ImageFileUpload
import backend.image.api.model.{ImageDto, ImageExifDto, ImplicitDtoConversion}
import backend.image.entities.ImageIO
import backend.image.interactors.ImageService
import backend.exif.interactors.ImageExifService
import sttp.capabilities.akka.AkkaStreams

import java.io.File
import java.time.Instant
import scala.concurrent.{ExecutionContext, Future}

class ApiService(service: ImageService,
                 exifService: ImageExifService,
                 validator: ApiServiceValidator)
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

  def listImages: Future[EnvelopedHttpResponse[Seq[ImageDto]]] =
    (for {
      ids <- service.listImageIds
      exifList <- exifService.listExif(Some(ids))
      widthById = exifList.map { case (imageId, exif) => imageId -> exif.width }.toMap
      heightById = exifList.map { case (imageId, exif) => imageId -> exif.height }.toMap
      dateById = exifList.map { case (imageId, exif) => imageId -> exif.date.getOrElse(Instant.now) }.toMap
    } yield ids
      .sortWith { case (a, b) => dateById(a).isAfter(dateById(b)) }
      .map(id => ImageDto(id, widthById.getOrElse(id, 1), heightById.getOrElse(id, 1))))
      .toEnvelopedHttpResponse

  def getImageExif(imageId: String): Future[EnvelopedHttpResponse[ImageExifDto]] =
    exifService.getExif(imageId).map {
      case Some(exif) => Right(exif.toDto).asInstanceOf[Right[HttpError, ImageExifDto]]
      case None => Left(NotFound(s"[imageId: $imageId] Metadata not found"))
    }.toEnveloped

  def uploadImage(form: ImageFileUpload): Future[HttpResponse[Unit]] =
    (for {
      fileName <- validator.fileHasFileName(form.image).toEitherT
      _ <- validator.fileIsJPG(fileName).toEitherT
      _ <- validator.imageDoesNotExist(fileName).toEitherT
      exif = ExifUtil.getExif(form.image.body)
      _ <- exifService.addExif(fileName, exif).toEitherT[HttpError]
      _ <- resizeAndUploadImage(fileName, form.image.body).toEitherT[HttpError]
      _ <- resizeAndUploadImage(fileName, form.image.body, MAX_THUMBNAIL_SIZE, service.uploadThumbnail).toEitherT[HttpError]
    } yield (): Unit).value

  private def resizeAndUploadImage(fileName: String,
                                   file: File,
                                   maxSize: Int = MAX_IMAGE_SIZE,
                                   upload: (String, Array[Byte]) => Future[Unit] = service.uploadImage): Future[Unit] = {
    val resizedImage = ImageResizer.resizeImage(file, maxSize)
    upload(fileName, resizedImage)
  }

  def removeImage(imageId: String): Future[HttpResponse[Unit]] = {
    (for {
      _ <- validator.imageExists(imageId).toEitherT
      _ <- service.removeThumbnail(imageId).toEitherT[HttpError]
      _ <- exifService.removeExif(imageId).toEitherT[HttpError]
      _ <- service.removeImage(imageId).toEitherT[HttpError]
    } yield (): Unit).value
  }

}
