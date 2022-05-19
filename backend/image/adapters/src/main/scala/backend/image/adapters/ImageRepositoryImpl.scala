package backend.image.adapters

import akka.stream.scaladsl.FileIO
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

  def getImageStream(imageId: String): Future[Option[ImageStream]] =
    getFileStream(fullPath(imageId, ImageType.FullSize))

  def uploadImage(file: File): Future[Unit] =
    copy(file, fullPath(file.getName, ImageType.FullSize))

  def removeImage(imageId: String): Future[Unit] =
    deleteFile(fullPath(imageId, ImageType.FullSize))
      .map(_ => (): Unit)

  def getThumbnailStream(imageId: String): Future[Option[ImageStream]] =
    getFileStream(fullPath(imageId, ImageType.Thumbnail))

  def uploadThumbnail(fileName: String, bytes: Array[Byte]): Future[Unit] =
    createFile(fullPath(fileName, ImageType.Thumbnail), bytes)

  def removeThumbnail(imageId: String): Future[Unit] =
    deleteFile(fullPath(imageId, ImageType.Thumbnail))
      .map(_ => (): Unit)

  private def getFileStream(filePath: Path): Future[Option[ImageStream]] =
    Future(Option.when(filePath.toFile.exists)(FileIO.fromPath(filePath)))

  private def createFile(path: Path, bytes: Array[Byte]): Future[Unit] =
    Future {
      Files.write(path, bytes)
      (): Unit
    }

  private def copy(file: File, destination: Path): Future[Unit] =
    Future {
      Files.createDirectories(destination.getParent)
      Files.copy(file.toPath, destination, StandardCopyOption.REPLACE_EXISTING)
      (): Unit
    }.recover {
      case e: IOException =>
        throw new RuntimeException(s"Could not upload file=${file.getPath}", e)
    }

  private def deleteFile(filePath: Path): Future[Boolean] =
    Future {
      if (Files.isRegularFile(filePath)) {
        Files.delete(filePath)
        true
      } else {
        false
      }
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
