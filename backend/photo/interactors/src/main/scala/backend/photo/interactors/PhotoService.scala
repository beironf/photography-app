package backend.photo.interactors

import backend.photo.entities.meta.Category.Category
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

  def listPhotos(category: Option[Category] = None,
                 group: Option[String] = None,
                 rating: Option[Int] = None,
                 inShowroom: Option[Boolean] = None): Future[Seq[Photo]] =
    repository.listPhotos(category, group, rating, inShowroom)

  def listPhotoGroups: Future[Seq[String]] =
    repository.listPhotoGroups

}
