package backend.photography.interactors.validation

import backend.photography.entities.exif.ImageExif
import backend.photography.entities.photo.Photo
import backend.photography.entities.response.Exceptions.*
import backend.photography.entities.response.Response.PhotographyResponse
import backend.photography.ports.repositories.{ImageExifRepository, ImageRepository, PhotoRepository}
import cats.implicits.catsSyntaxEitherId

import scala.concurrent.{ExecutionContext, Future}

class Validator(photoRepository: PhotoRepository,
                imageRepository: ImageRepository,
                exifRepository: ImageExifRepository)
               (implicit executionContext: ExecutionContext) {

  private val rightUnit = ((): Unit).asRight

  def photoExists(imageId: String): Future[PhotographyResponse[Photo]] =
    photoRepository.getPhoto(imageId).map {
      case Some(photo) => photo.asRight
      case _ => PhotoNotFoundException(imageId).asLeft
    }

  def photoDoesNotExist(imageId: String): Future[PhotographyResponse[Unit]] =
    photoRepository.getPhoto(imageId).map {
      case Some(_) => PhotoAlreadyExistsException(imageId).asLeft
      case _ => rightUnit
    }

  def imageExists(imageId: String): Future[PhotographyResponse[String]] =
    imageRepository.listImageIds.map(_.contains(imageId)).map {
      case true => imageId.asRight
      case false => ImageNotFoundException(imageId).asLeft
    }

  def imageDoesNotExist(imageId: String): Future[PhotographyResponse[String]] =
    imageRepository.listImageIds.map(_.contains(imageId)).map {
      case true => ImageAlreadyExistsException(imageId).asLeft
      case false => imageId.asRight
    }

  def thumbnailExists(imageId: String): Future[PhotographyResponse[String]] =
    imageRepository.listThumbnailIds.map(_.contains(imageId)).map {
      case true => imageId.asRight
      case false => ThumbnailNotFoundException(imageId).asLeft
    }

  def siteImageExists(fileName: String): Future[PhotographyResponse[String]] =
    imageRepository.listSiteImageFileNames.map(_.contains(fileName)).map {
      case true => fileName.asRight
      case false => SiteImageNotFoundException(fileName).asLeft
    }

  def fileIsJPG(fileName: String): PhotographyResponse[Unit] =
    if (fileName.endsWith(".jpg")) rightUnit
    else FileIsNotJPGException(fileName).asLeft

  def fileNameIsDefined(fileName: Option[String]): PhotographyResponse[String] =
    fileName.map(_.asRight)
      .getOrElse(FileNameNotDefinedException.asLeft)

  def exifExists(imageId: String): Future[PhotographyResponse[ImageExif]] =
    exifRepository.getExif(imageId).map {
      case Some(exif) => exif.asRight
      case _ => ExifNotFoundException(imageId).asLeft
    }

}
