package backend.image.adapters

import backend.core.application.DefaultService
import backend.image.adapters.ImageType.ImageType
import backend.image.entities.ImageIO
import backend.image.ports.ImageRepository

import java.io.{File, IOException}
import java.nio.file.{Files, Path, Paths, StandardCopyOption}
import scala.concurrent.{ExecutionContext, Future}

object ImageRepositoryImpl extends DefaultService {
  val IMAGE_PATH: Path = Paths.get(config.getString("file-storage.images.local.path"))
  val THUMBNAIL_PATH: Path = Paths.get(config.getString("file-storage.thumbnails.local.path"))

  def apply()(implicit executionContext: ExecutionContext): ImageRepositoryImpl =
    new ImageRepositoryImpl
}

class ImageRepositoryImpl()
                         (implicit executionContext: ExecutionContext) extends ImageRepository with ImageIO {
  import backend.image.adapters.ImageRepositoryImpl._

  dirExists(ImageType.FullSize)
  dirExists(ImageType.Thumbnail)

  def getImageStream(imageId: String): Future[Option[ImageStream]] = ???

  def uploadImage(file: File): Future[Unit] =
    copy(file, fullPath(file.getName, ImageType.FullSize))

  def removeImage(imageId: String): Future[Unit] = ???

  def getThumbnailStream(imageId: String): Future[Option[ImageStream]] = ???

  def uploadThumbnail(file: File): Future[Unit] =
    copy(file, fullPath(file.getName, ImageType.Thumbnail))

  def removeThumbnail(imageId: String): Future[Unit] = ???

  private def copy(file: File, destination: Path): Future[Unit] =
    Future.successful {
      Files.createDirectories(destination.getParent)
      Files.copy(file.toPath, destination, StandardCopyOption.REPLACE_EXISTING)
      (): Unit
    }.recover {
      case e: IOException =>
        throw new RuntimeException(s"Could not upload file=${file.getPath}", e)
    }

  private def dirExists(`type`: ImageType): Unit =
    if (!getDirPath(`type`).toFile.exists())
      throw new RuntimeException(s"Could not find directory at ${getDirPath(`type`)}")

  private def fullPath(fileName: String, `type`: ImageType): Path = getDirPath(`type`) resolve fileName

  private def getDirPath(`type`: ImageType): Path =
    `type` match {
      case ImageType.FullSize => IMAGE_PATH
      case ImageType.Thumbnail => THUMBNAIL_PATH
    }

}