package backend.photography.ports.interactors

import backend.photography.entities.image.ImageIO
import backend.photography.entities.photo.meta.Category.Category
import backend.photography.entities.photo.{Photo, UpdatePhoto}
import backend.photography.entities.response.Response.PhotographyResponse

import scala.concurrent.Future

trait PhotoService extends ImageIO {

  def getPhoto(imageId: String): Future[PhotographyResponse[Photo]]

  def addPhoto(photo: Photo): Future[PhotographyResponse[Unit]]

  def updatePhoto(imageId: String, update: UpdatePhoto): Future[PhotographyResponse[Unit]]

  def listPhotos(category: Option[Category] = None,
                 group: Option[String] = None,
                 rating: Option[Int] = None,
                 inShowroom: Option[Boolean] = None): Future[PhotographyResponse[Seq[Photo]]]

  def listPhotoGroups: Future[PhotographyResponse[Seq[String]]]

}
