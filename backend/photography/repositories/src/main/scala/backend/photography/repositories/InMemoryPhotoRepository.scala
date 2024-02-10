package backend.photography.repositories

import backend.photography.entities.photo.meta.Category.Category
import backend.photography.entities.photo.{Photo, UpdatePhoto}
import backend.photography.ports.repositories.PhotoRepository

import scala.concurrent.{ExecutionContext, Future}

object InMemoryPhotoRepository {
  def apply()(implicit executionContext: ExecutionContext): InMemoryPhotoRepository =
    new InMemoryPhotoRepository()
}

class InMemoryPhotoRepository()
                             (implicit executionContext: ExecutionContext) extends PhotoRepository {
  var photos: Seq[Photo] = Seq.empty[Photo]

  def getPhoto(imageId: String): Future[Option[Photo]] = Future.successful {
    photos.find(_.imageId == imageId)
  }

  def listPhotos(categoryOpt: Option[Category] = None,
                 groupOpt: Option[String] = None,
                 ratingOpt: Option[Int] = None,
                 inShowroomOpt: Option[Boolean] = None): Future[Seq[Photo]] = Future.successful {
    photos.filter(p =>
      categoryOpt.forall(_ == p.metadata.category) &&
        groupOpt.forall(group => p.group.contains(group)) &&
        ratingOpt.forall(_ == p.judgement.rating) &&
        inShowroomOpt.forall(_ == p.judgement.inShowroom)
    )
  }

  def listPhotoGroups: Future[Seq[String]] = Future.successful {
    photos.groupBy(_.group).keys.flatten.toSeq
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
