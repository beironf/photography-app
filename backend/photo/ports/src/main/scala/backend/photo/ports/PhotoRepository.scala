package backend.photo.ports

import backend.photo.entities._
import backend.photo.entities.meta.Category.Category

import scala.concurrent.Future

trait PhotoRepository {

  def getPhoto(imageId: String): Future[Option[Photo]]

  def addPhoto(photo: Photo): Future[Unit]

  def updatePhoto(imageId: String, update: UpdatePhoto): Future[Unit]

  def removePhoto(imageId: String): Future[Unit]

  def listPhotos(category: Option[Category] = None,
                 group: Option[String] = None,
                 rating: Option[Int] = None,
                 inShowroom: Option[Boolean] = None): Future[Seq[Photo]]

}
