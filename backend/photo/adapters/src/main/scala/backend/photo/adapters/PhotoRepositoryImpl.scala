package backend.photo.adapters

import backend.photo.entities._
import backend.photo.entities.meta._
import backend.photo.ports.PhotoRepository

import scala.concurrent.{ExecutionContext, Future}

object PhotoRepositoryImpl {
  def apply()(implicit executionContext: ExecutionContext): PhotoRepositoryImpl =
    new PhotoRepositoryImpl
}

class PhotoRepositoryImpl()(implicit executionContext: ExecutionContext) extends PhotoRepository {

  def getPhoto(imageId: String): Future[Option[Photo]] = ???

  def addPhoto(photo: Photo): Future[Unit] = ???

  def removePhoto(imageId: String): Future[Unit] = ???

  def listPhotos(category: Option[Category.Value] = None,
                 rating: Option[Int] = None): Future[Seq[Photo]] = ???

}
