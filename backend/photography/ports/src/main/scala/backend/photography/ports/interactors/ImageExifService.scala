package backend.photography.ports.interactors

import backend.photography.entities.exif.ImageExif
import backend.photography.entities.image.ImageIO
import backend.photography.entities.response.Response.PhotographyResponse

import scala.concurrent.Future

trait ImageExifService extends ImageIO {

  def getExif(imageId: String): Future[PhotographyResponse[ImageExif]]

  def listExif(imageIds: Option[Seq[String]] = None): Future[PhotographyResponse[Seq[(String, ImageExif)]]]

}
