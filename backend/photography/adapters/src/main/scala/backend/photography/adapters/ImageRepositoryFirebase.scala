package backend.photography.adapters

import akka.stream.scaladsl.StreamConverters
import backend.core.application.DefaultService
import backend.photography.adapters.model.image.FileId
import backend.photography.ports.ImageRepository
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.WriteChannel
import com.google.cloud.storage.Storage.BlobListOption
import com.google.cloud.storage.{Blob, BlobId, BlobInfo, StorageOptions}

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.channels.Channels
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters.*
import scala.util.{Failure, Success, Try}

object ImageRepositoryFirebase extends DefaultService {
  private val FirebaseCredentials = config.getString("firebase.credentials")
  private val BucketName = config.getString("firebase.storage.bucket_name")
  private val ImageDirectory = config.getString("file-storage.images.dir")
  private val ThumbnailDirectory = config.getString("file-storage.thumbnails.dir")
  private val SiteImageDirectory = config.getString("file-storage.site-images.dir")

  def apply()(implicit executionContext: ExecutionContext): ImageRepositoryFirebase =
    new ImageRepositoryFirebase()
}

class ImageRepositoryFirebase()
                             (implicit executionContext: ExecutionContext) extends ImageRepository with DefaultService {
  import ImageRepositoryFirebase.*

  private def toFileId(fileName: String, directory: String) = FileId(BucketName, s"$directory/$fileName")

  private def toBlobId(fileId: FileId) = BlobId.of(fileId.bucket, fileId.path)

  private val credentialsStream = new ByteArrayInputStream(FirebaseCredentials.getBytes)
  private lazy val storage = StorageOptions.newBuilder
    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
    .build
    .getService

  def listImageIds: Future[Seq[String]] =
    listBlobNames(ImageDirectory)

  def getImageStream(imageId: String): Future[Option[ImageStream]] =
    getFileStream(toFileId(imageId, ImageDirectory))

  def uploadImage(fileName: String, bytes: Array[Byte]): Future[Unit] =
    uploadFile(toFileId(fileName, ImageDirectory), bytes)

  def removeImage(imageId: String): Future[Unit] =
    deleteFile(toFileId(imageId, ImageDirectory))

  def getThumbnailStream(imageId: String): Future[Option[ImageStream]] =
    getFileStream(toFileId(imageId, ThumbnailDirectory))

  def uploadThumbnail(fileName: String, bytes: Array[Byte]): Future[Unit] =
    uploadFile(toFileId(fileName, ThumbnailDirectory), bytes)

  def removeThumbnail(imageId: String): Future[Unit] =
    deleteFile(toFileId(imageId, ThumbnailDirectory))

  def getSiteImageStream(fileName: String): Future[Option[ImageStream]] =
    getFileStream(toFileId(fileName, SiteImageDirectory))

  private def listBlobNames(directory: String): Future[Seq[String]] = Future {
    storage.list(BucketName, BlobListOption.currentDirectory, BlobListOption.prefix(s"$directory/"))
      .iterateAll().asScala
      .map(_.getName.split("/").last)
      .filter(_.endsWith(".jpg"))
      .toSeq
  }

  private def getFileStream(fileId: FileId): Future[Option[ImageStream]] = Future {
    val blobId = toBlobId(fileId)
    getBlobInputStream(blobId) match {
      case Success(Some(inputStream)) => Some(StreamConverters.fromInputStream(() => inputStream))
      case Success(None) => None
      case Failure(e) =>
        logger.error(s"Failed to download file with blobId=$blobId", e)
        None
    }
  }

  private def getBlobInputStream(blobId: BlobId): Try[Option[InputStream]] =
    getBlob(blobId).map(_.map(blob => Channels.newInputStream(blob.reader())))

  private def getBlob(blobId: BlobId): Try[Option[Blob]] = Try {
    Option(storage.get(blobId))
  }

  private def uploadFile(fileId: FileId, bytes: Array[Byte]): Future[Unit] = Future {
    val blobId = toBlobId(fileId)
    val blobInfo: BlobInfo = BlobInfo.newBuilder(blobId).build()
    val input: InputStream = new ByteArrayInputStream(bytes)
    uploadInputStream(blobInfo, input)
    (): Unit
  }

  private def uploadInputStream(blobInfo: BlobInfo, input: InputStream): UploadResult = {
    val writer: WriteChannel = storage.writer(blobInfo)
    val os = Channels.newOutputStream(writer)
    try {
      val transferredBytes = input.transferTo(os)
      UploadResult(transferredBytes)
    } catch {
      case e: Exception =>
        throw new RuntimeException(s"Uploading file failed for blobId=${blobInfo.getBlobId}", e)
    } finally {
      writer.close()
      os.close()
    }
  }

  private def deleteFile(fileId: FileId): Future[Unit] = Future {
    storage.delete(toBlobId(fileId))
    (): Unit
  }

}
