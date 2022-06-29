package backend.photo.interactors

import backend.photo.entities.{Photo, UpdatePhoto}
import backend.photo.ports.PhotoRepository

import scala.concurrent.Future

class PhotoService(repository: PhotoRepository) {

  def getPhoto(imageId: String): Future[Option[Photo]] =
    repository.getPhoto(imageId)

  def addPhoto(photo: Photo): Future[Unit] =
    repository.addPhoto(photo)

  def updatePhoto(imageId: String, update: UpdatePhoto): Future[Unit] =
    repository.updatePhoto(imageId, update)

  def removePhoto(imageId: String): Future[Unit] =
    repository.removePhoto(imageId)

  def listPhotos(group: Option[String] = None,
                 rating: Option[Int] = None): Future[Seq[Photo]] =
    repository.listPhotos(group, rating)

}
