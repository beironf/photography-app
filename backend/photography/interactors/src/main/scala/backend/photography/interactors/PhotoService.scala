package backend.photography.interactors

import backend.core.utils.EitherTExtensions
import backend.photography.entities.photo.meta.Category.Category
import backend.photography.entities.photo.{Photo, UpdatePhoto}
import backend.photography.entities.response.Exceptions.PhotographyException
import backend.photography.entities.response.Response.PhotographyResponse
import backend.photography.ports.PhotoRepository

import scala.concurrent.{ExecutionContext, Future}

object PhotoService {
  def apply(validator: Validator,
            repository: PhotoRepository)
           (implicit executionContext: ExecutionContext): PhotoService =
    new PhotoService(validator, repository)
}

class PhotoService(validator: Validator,
                   repository: PhotoRepository)
                  (implicit executionContext: ExecutionContext) extends EitherTExtensions {

  def getPhoto(imageId: String): Future[PhotographyResponse[Photo]] =
    validator.photoExists(imageId)

  def addPhoto(photo: Photo): Future[PhotographyResponse[Unit]] = (for {
    _ <- validator.photoDoesNotExist(photo.imageId).toEitherT
    _ <- repository.addPhoto(photo).toEitherT[PhotographyException]
  } yield (): Unit).value

  def updatePhoto(imageId: String, update: UpdatePhoto): Future[PhotographyResponse[Unit]] = (for {
    _ <- validator.photoExists(imageId).toEitherT
    _ <- repository.updatePhoto(imageId, update).toEitherT[PhotographyException]
  } yield (): Unit).value

  def removePhoto(imageId: String): Future[PhotographyResponse[Unit]] = (for {
    _ <- validator.photoExists(imageId).toEitherT
    _ <- repository.removePhoto(imageId).toEitherT[PhotographyException]
  } yield (): Unit).value

  def listPhotos(category: Option[Category] = None,
                 group: Option[String] = None,
                 rating: Option[Int] = None,
                 inShowroom: Option[Boolean] = None): Future[PhotographyResponse[Seq[Photo]]] = (for {
    photos <- repository.listPhotos(category, group, rating, inShowroom).toEitherT[PhotographyException]
  } yield photos.sortBy(_.taken).reverse).value

  def listPhotoGroups: Future[PhotographyResponse[Seq[String]]] =
    repository.listPhotoGroups.toEitherT[PhotographyException].value

}
