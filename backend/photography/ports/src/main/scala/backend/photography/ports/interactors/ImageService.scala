package backend.photography.ports.interactors

import backend.photography.entities.image.ImageIO
import backend.photography.entities.response.Response.PhotographyResponse

import java.io.File
import scala.concurrent.Future

trait ImageService extends ImageIO {

  def listImageIds: Future[PhotographyResponse[Seq[String]]]

  def getImageStream(imageId: String): Future[PhotographyResponse[ImageStream]]

  def uploadImage(fileNameOpt: Option[String], image: File): Future[PhotographyResponse[Unit]]

  def removeImage(imageId: String): Future[PhotographyResponse[Unit]]

  def getThumbnailStream(imageId: String): Future[PhotographyResponse[ImageStream]]

  def getSiteImageStream(fileName: String): Future[PhotographyResponse[ImageStream]]

}
