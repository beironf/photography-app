package backend.photography.adapters

import akka.stream.scaladsl.FileIO
import backend.core.application.DefaultService
import backend.photography.entities.image.ImageType.ImageType
import backend.photography.entities.image.*
import backend.photography.ports.ImageRepository

import java.io.IOException
import java.nio.file.{Files, Path, Paths}
import scala.concurrent.{ExecutionContext, Future}

object ImageRepositoryLocal extends DefaultService {
  private val ImagesPath: Path = Paths.get(config.getString("file-storage.images.dir"))
  private val ThumbnailsPath: Path = Paths.get(config.getString("file-storage.thumbnails.dir"))
  private val SiteImagesPath: Path = Paths.get(config.getString("file-storage.site-images.dir"))

  def apply()(implicit executionContext: ExecutionContext): ImageRepositoryLocal = new ImageRepositoryLocal()
}

class ImageRepositoryLocal()
                          (implicit executionContext: ExecutionContext) extends ImageRepository with ImageIO {
  import ImageRepositoryLocal.*

  def listImageIds: Future[Seq[String]] = Future(
    ImagesPath.toFile.listFiles
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

  def listThumbnailIds: Future[Seq[String]] = Future(
    ThumbnailsPath.toFile.listFiles
      .filter(f => f.isFile)
      .map(_.getName)
      .filter(_.endsWith(".jpg"))
      .toSeq
  )

  def getThumbnailStream(imageId: String): Future[Option[ImageStream]] =
    getFileStream(fullPath(imageId, ImageType.Thumbnail))

  def uploadThumbnail(fileName: String, bytes: Array[Byte]): Future[Unit] =
    createFile(fullPath(fileName, ImageType.Thumbnail), bytes)

  def removeThumbnail(imageId: String): Future[Unit] =
    deleteFile(fullPath(imageId, ImageType.Thumbnail))
      .map(_ => (): Unit)

  def listSiteImageFileNames: Future[Seq[String]] = Future(
    SiteImagesPath.toFile.listFiles
      .filter(f => f.isFile)
      .map(_.getName)
      .filter(name => name.endsWith(".jpg") || name.endsWith(".png"))
      .toSeq
  )

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

  def necessaryPathsExist(): Unit = {
    dirExists(ImageType.FullSize)
    dirExists(ImageType.Thumbnail)
    dirExists(ImageType.Site)
  }

  private def dirExists(`type`: ImageType): Unit =
    if (!getDirPath(`type`).toFile.exists())
      throw new RuntimeException(s"Could not find directory at ${getDirPath(`type`)}")

  private def fullPath(fileName: String, `type`: ImageType): Path = getDirPath(`type`) resolve fileName

  private def getDirPath(`type`: ImageType): Path =
    `type` match {
      case ImageType.FullSize => ImagesPath
      case ImageType.Thumbnail => ThumbnailsPath
      case ImageType.Site => SiteImagesPath
    }

}
