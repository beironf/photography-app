package backend.photo.adapters

import backend.photo.entities.{Photo, UpdatePhoto}
import backend.photo.ports.PhotoRepository

import scala.concurrent.{ExecutionContext, Future}


class InMemoryPhotoRepository()
                             (implicit executionContext: ExecutionContext) extends PhotoRepository {
  var photos: Seq[Photo] = Seq.empty[Photo]

  def getPhoto(imageId: String): Future[Option[Photo]] = Future.successful {
    photos.find(_.imageId == imageId)
  }

  def listPhotos(groupOpt: Option[String] = None,
                 ratingOpt: Option[Int] = None): Future[Seq[Photo]] = Future.successful {
    photos.filter(p =>
      groupOpt.forall(group => p.group.contains(group)) &&
        ratingOpt.forall(_ == p.judgement.rating)
    )
  }

  def addPhoto(photo: Photo): Future[Unit] = Future.successful {
    photos = photos.appended(photo)
  }

  def updatePhoto(imageId: String, update: UpdatePhoto): Future[Unit] = for {
    _ <- removePhoto(imageId)
    _ <- addPhoto(update.toPhoto(imageId))
  } yield (): Unit

  def removePhoto(imageId: String): Future[Unit] = Future.successful {
    photos = photos.filterNot(_.imageId == imageId)
  }
}
