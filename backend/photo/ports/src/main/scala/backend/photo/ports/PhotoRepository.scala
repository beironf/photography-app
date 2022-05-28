package backend.photo.ports

import backend.photo.entities._

import scala.concurrent.Future

trait PhotoRepository {

  def getPhoto(imageId: String): Future[Option[Photo]]

  def addPhoto(photo: Photo): Future[Unit]

  def removePhoto(imageId: String): Future[Unit]

  def listPhotos(group: Option[String] = None,
                 rating: Option[Int] = None): Future[Seq[Photo]]

}
