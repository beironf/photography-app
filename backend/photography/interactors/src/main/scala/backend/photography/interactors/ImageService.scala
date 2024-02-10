package backend.photography.interactors

import backend.core.utils.EitherTExtensions
import backend.photography.entities.image.ImageIO
import backend.photography.entities.response.Exceptions.PhotographyException
import backend.photography.entities.response.Response.PhotographyResponse
import backend.photography.interactors.ImageService.{MAX_IMAGE_SIZE, MAX_THUMBNAIL_SIZE}
import backend.photography.interactors.utils.{ExifUtil, ImageResizer}
import backend.photography.interactors.validation.Validator
import backend.photography.ports.repositories.{ImageExifRepository, ImageRepository}

import java.io.File
import scala.concurrent.{ExecutionContext, Future}

object ImageService {
  def apply(validator: Validator,
            imageRepository: ImageRepository,
            exifRepository: ImageExifRepository)
           (implicit executionContext: ExecutionContext): ImageService =
    new ImageService(validator, imageRepository, exifRepository)

  private val MAX_IMAGE_SIZE = 4000
  private val MAX_THUMBNAIL_SIZE = 1200
}

class ImageService(validator: Validator,
                   imageRepository: ImageRepository,
                   exifRepository: ImageExifRepository)
                  (implicit executionContext: ExecutionContext) extends EitherTExtensions with ImageIO {

  def listImageIds: Future[PhotographyResponse[Seq[String]]] =
    imageRepository.listImageIds.toEitherT[PhotographyException].value

  def getImageStream(imageId: String): Future[PhotographyResponse[ImageStream]] = (for {
    _ <- validator.imageExists(imageId).toEitherT
    streamOpt <- imageRepository.getImageStream(imageId).toEitherT[PhotographyException]
    stream = streamOpt.getOrElse(throw new RuntimeException(s"Image stream is None, imageId: $imageId"))
  } yield stream).value

  def uploadImage(fileNameOpt: Option[String], image: File): Future[PhotographyResponse[Unit]] = (for {
    fileName <- validator.fileNameIsDefined(fileNameOpt).toEitherT[Future]
    _ <- validator.fileIsJPG(fileName).toEitherT[Future]
    _ <- validator.imageDoesNotExist(fileName).toEitherT
    exif = ExifUtil.getExif(image)
    _ <- exifRepository.addExif(fileName, exif).toEitherT[PhotographyException]
    _ <- resizeAndUploadImage(fileName, image, MAX_IMAGE_SIZE, imageRepository.uploadImage).toEitherT[PhotographyException]
    _ <- resizeAndUploadImage(fileName, image, MAX_THUMBNAIL_SIZE, imageRepository.uploadThumbnail).toEitherT[PhotographyException]
  } yield (): Unit).value

  private def resizeAndUploadImage(fileName: String,
                                   file: File,
                                   maxSize: Int,
                                   upload: (String, Array[Byte]) => Future[Unit]): Future[Unit] = {
    val resizedImage = ImageResizer.resizeImage(file, maxSize)
    upload(fileName, resizedImage)
  }

  def removeImage(imageId: String): Future[PhotographyResponse[Unit]] = (for {
    _ <- validator.imageExists(imageId).toEitherT
    _ <- imageRepository.removeThumbnail(imageId).toEitherT[PhotographyException]
    _ <- exifRepository.removeExif(imageId).toEitherT[PhotographyException]
    _ <- imageRepository.removeImage(imageId).toEitherT[PhotographyException]
  } yield (): Unit).value

  def getThumbnailStream(imageId: String): Future[PhotographyResponse[ImageStream]] = (for {
    _ <- validator.thumbnailExists(imageId).toEitherT
    streamOpt <- imageRepository.getThumbnailStream(imageId).toEitherT[PhotographyException]
    stream = streamOpt.getOrElse(throw new RuntimeException(s"Thumbnail stream is None, imageId: $imageId"))
  } yield stream).value

  def getSiteImageStream(fileName: String): Future[PhotographyResponse[ImageStream]] = (for {
    _ <- validator.siteImageExists(fileName).toEitherT
    streamOpt <- imageRepository.getSiteImageStream(fileName).toEitherT[PhotographyException]
    stream = streamOpt.getOrElse(throw new RuntimeException(s"Site image stream is None, fileName: $fileName"))
  } yield stream).value

}
