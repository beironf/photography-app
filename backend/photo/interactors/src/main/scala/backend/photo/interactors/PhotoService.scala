package backend.photo.interactors

import backend.photo.entities.Photo
import backend.photo.entities.meta._
import backend.photo.ports.PhotoRepository

import scala.concurrent.Future

class PhotoService(repository: PhotoRepository) {

  def getPhoto(imageId: String): Future[Option[Photo]] =
    repository.getPhoto(imageId)

  def addPhoto(photo: Photo): Future[Unit] =
    repository.addPhoto(photo)

  def removePhoto(imageId: String): Future[Unit] =
    repository.removePhoto(imageId)

  def listPhotos(category: Option[Category.Value] = None,
                 rating: Option[Int] = None): Future[Seq[Photo]] =
    repository.listPhotos(category, rating)

}
