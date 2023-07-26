package backend.image.adapters

import akka.stream.scaladsl.FileIO
import backend.core.application.DefaultService
import backend.image.entities.ImageType.ImageType
import backend.image.entities._
import backend.image.ports.ImageRepository

import java.io.IOException
import java.nio.file.{Files, Path, Paths}
import scala.concurrent.{ExecutionContext, Future}

object ImageRepositoryImpl extends DefaultService {
  val IMAGE_PATH: Path = Paths.get(config.getString("file-storage.images.development.path"))
  val THUMBNAIL_PATH: Path = Paths.get(config.getString("file-storage.thumbnails.development.path"))
  val SITE_IMAGE_PATH: Path = Paths.get(config.getString("file-storage.site-images.development.path"))

  def apply()(implicit executionContext: ExecutionContext): ImageRepositoryImpl =
    new ImageRepositoryImpl
}

class ImageRepositoryImpl()
                         (implicit executionContext: ExecutionContext) extends ImageRepository with ImageIO {
  import backend.image.adapters.ImageRepositoryImpl._

  dirExists(ImageType.FullSize)
  dirExists(ImageType.Thumbnail)

  def listImageIds: Future[Seq[String]] = Future(
    IMAGE_PATH.toFile.listFiles
      .filter(f => f.isFile)
      .map(_.getName)
      .filter(_.endsWith(".jpg"))
      .toSeq
  )

  def getImageStream(imageId: String): Future[Option[ImageStream]] =
    getFileStream(fullPath(imageId, ImageType.FullSize))

  def uploadImage(fileName: String, bytes: Array[Byte]): Future[Unit] =
    createFile(fullPath(fileName, ImageType.FullSize), bytes)

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

  def getSiteImageStream(fileName: String): Future[Option[ImageStream]] =
    getFileStream(fullPath(fileName, ImageType.Site))

  private def getFileStream(filePath: Path): Future[Option[ImageStream]] =
    Future(Option.when(filePath.toFile.exists)(FileIO.fromPath(filePath)))

  private def createFile(path: Path, bytes: Array[Byte]): Future[Unit] =
    Future {
      Files.createDirectories(path.getParent)
      Files.write(path, bytes)
      (): Unit
    }.recover {
      case e: IOException =>
        throw new RuntimeException(s"Could not create file=$path", e)
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
      case ImageType.Site => SITE_IMAGE_PATH
    }

}
