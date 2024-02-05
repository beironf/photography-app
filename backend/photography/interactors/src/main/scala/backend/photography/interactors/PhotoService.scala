package backend.photography.interactors

import backend.core.utils.EitherTExtensions
import backend.photography.entities.photo.meta.Category.Category
import backend.photography.entities.photo.{Photo, UpdatePhoto}
import backend.photography.entities.response.Response.PhotographyResponse
import backend.photography.ports.PhotoRepository

import scala.concurrent.Future

object PhotoService {
  def apply(validator: Validator,
            repository: PhotoRepository): PhotoService =
    new PhotoService(validator, repository)
}

class PhotoService(validator: Validator,
                   repository: PhotoRepository) extends EitherTExtensions {

  def getPhoto(imageId: String): Future[PhotographyResponse[Photo]] =
    validator.photoExists(imageId)

  def addPhoto(photo: Photo): Future[Unit] =
    repository.addPhoto(photo)

  def updatePhoto(imageId: String, update: UpdatePhoto): Future[Unit] =
    repository.updatePhoto(imageId, update)

  def removePhoto(imageId: String): Future[Unit] =
    repository.removePhoto(imageId)

  def listPhotos(category: Option[Category] = None,
                 group: Option[String] = None,
                 rating: Option[Int] = None,
                 inShowroom: Option[Boolean] = None): Future[Seq[Photo]] = for {
    cat <- Future.apply(category.map(_.toDomain))
      .recover { case _: NoSuchElementException => throw BadRequestException(s"'${category.get}' is not a valid category") }
    photos <- repository.listPhotos(category, group, rating, inShowroom)
    exifList <- exifService.listExif(Some(photos.map(_.imageId)))
    width = exifList.map { case (imageId, exif) => imageId -> exif.width }.toMap
    height = exifList.map { case (imageId, exif) => imageId -> exif.height }.toMap
  } yield photos.sortBy(_.taken).reverse.map { photo =>
    photo.toDtoWithRatio(width.getOrElse(photo.imageId, 1), height.getOrElse(photo.imageId, 1))
  }

  def listPhotoGroups: Future[Seq[String]] =
    repository.listPhotoGroups

}
