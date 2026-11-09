package backend.photography.interactors

import backend.core.utils.EitherTExtensions
import backend.photography.entities.response.Exceptions.PhotographyException
import backend.photography.entities.response.Response.PhotographyResponse
import backend.photography.interactors.ImageServiceImpl.{MAX_IMAGE_SIZE, MAX_THUMBNAIL_SIZE, processImage}
import backend.photography.interactors.utils.{ExifUtil, ImageResizer}
import backend.photography.interactors.validation.{Validator, ValidatorImplicits}
import backend.photography.ports.interactors.ImageService
import backend.photography.ports.repositories.{ImageExifRepository, ImageRepository, PhotoRepository}

import java.io.File
import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object ImageServiceImpl {
  def apply(validator: Validator,
            imageRepository: ImageRepository,
            exifRepository: ImageExifRepository,
            photoRepository: PhotoRepository)
           (implicit executionContext: ExecutionContext): ImageService =
    new ImageServiceImpl(validator, imageRepository, exifRepository, photoRepository)

  private val MAX_IMAGE_SIZE = 4000
  private val MAX_THUMBNAIL_SIZE = 1200

  // Images are processed one at a time to keep the memory usage bounded
  private val IMAGE_PROCESSING_CONTEXT = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  // A full gc afterwards lets the jvm shrink the heap and return the memory used for decoding to the OS
  private def processImage[T](process: => T): Future[T] =
    Future(try process finally System.gc())(IMAGE_PROCESSING_CONTEXT)
}

class ImageServiceImpl(validator: Validator,
                       imageRepository: ImageRepository,
                       exifRepository: ImageExifRepository,
                       photoRepository: PhotoRepository)
                      (implicit executionContext: ExecutionContext) extends ImageService
  with EitherTExtensions
  with ValidatorImplicits {

  def listImageIds: Future[PhotographyResponse[Seq[String]]] =
    imageRepository.listImageIds.toEitherT[PhotographyException].value

  def getImageStream(imageId: String): Future[PhotographyResponse[ImageStream]] = (for {
    _ <- validator.imageExists(imageId).toEitherT
    stream <- imageRepository.getImageStream(imageId).toEitherT(
      ifNone = new PhotographyException(s"Image stream is None, imageId: $imageId"))
  } yield stream).value

  def uploadImage(fileNameOpt: Option[String], image: File): Future[PhotographyResponse[Unit]] = (for {
    fileName <- validator.fileNameIsDefined(fileNameOpt).toEitherT[Future]
    _ <- validator.fileIsJPG(fileName).toEitherT[Future]
    _ <- validator.imageDoesNotExist(fileName).toEitherT
    exif = ExifUtil.getExif(image)
    resized <- processImage(ImageResizer.resizeImage(image, Seq(MAX_IMAGE_SIZE, MAX_THUMBNAIL_SIZE))).toEitherT[PhotographyException]
    _ <- imageRepository.uploadImage(fileName, resized.head).toEitherT[PhotographyException]
    _ <- imageRepository.uploadThumbnail(fileName, resized.last).toEitherT[PhotographyException]
    // added last so that a failed upload does not leave an exif row behind
    _ <- exifRepository.addExif(fileName, exif).toEitherT[PhotographyException]
  } yield (): Unit).value

  def removeImage(imageId: String): Future[PhotographyResponse[Unit]] = (for {
    _ <- validator.imageExists(imageId).toEitherT
    _ <- imageRepository.removeThumbnail(imageId).toEitherT[PhotographyException]
    _ <- exifRepository.removeExif(imageId).toEitherT[PhotographyException]
    _ <- imageRepository.removeImage(imageId).toEitherT[PhotographyException]
    photoExists <- validator.photoExists(imageId).map(_.toBoolean).toEitherT
    _ <-
      if (photoExists) photoRepository.removePhoto(imageId).toEitherT[PhotographyException]
      else Future.successful(()).toEitherT[PhotographyException]
  } yield (): Unit).value

  def getThumbnailStream(imageId: String): Future[PhotographyResponse[ImageStream]] = (for {
    _ <- validator.thumbnailExists(imageId).toEitherT
    stream <- imageRepository.getThumbnailStream(imageId).toEitherT(
      ifNone = new PhotographyException(s"Thumbnail stream is None, imageId: $imageId"))
  } yield stream).value

  def getSiteImageStream(fileName: String): Future[PhotographyResponse[ImageStream]] = (for {
    _ <- validator.siteImageExists(fileName).toEitherT
    stream <- imageRepository.getSiteImageStream(fileName).toEitherT(
      ifNone = new PhotographyException(s"Site image stream is None, fileName: $fileName"))
  } yield stream).value

}
