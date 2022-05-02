package backend.photo.ports

import backend.photo.entities._
import backend.photo.entities.meta._

import scala.concurrent.Future

trait PhotoRepository {

  def getPhoto(imageId: String): Future[Option[Photo]]

  def addPhoto(photo: Photo): Future[Unit]

  def removePhoto(imageId: String): Future[Unit]

  def listPhotos(category: Option[Category.Value] = None,
                 rating: Option[Int] = None): Future[Seq[Photo]]

}
