package backend.image.adapters.exifdb

import backend.core.sqlstorage.DatabaseConnector

import java.time.Instant

trait ImagesExifTable extends DatabaseConnector {

  import dbConfig.profile.api._

  class ImagesExif(tag: Tag) extends Table[ImageExifDb](tag, "images_exif") {

    def imageId: Rep[String] = column[String]("image_id", O.PrimaryKey)
    def cameraMake: Rep[Option[String]] = column[Option[String]]("camera_make")
    def cameraModel: Rep[Option[String]] = column[Option[String]]("camera_model")
    def lens: Rep[Option[String]] = column[Option[String]]("lens")
    def focalLength: Rep[Option[Int]] = column[Option[Int]]("focal_length")
    def fNumber: Rep[Option[Float]] = column[Option[Float]]("f_number")
    def iso: Rep[Option[Int]] = column[Option[Int]]("iso")
    def exposureTime: Rep[Option[String]] = column[Option[String]]("exposureTime")
    def date: Rep[Option[Instant]] = column[Option[Instant]]("date")

    def * = (imageId, cameraMake, cameraModel, lens, focalLength, fNumber, iso,
      exposureTime, date) <> (ImageExifDb.tupled, ImageExifDb.unapply)
  }

  val imagesExif = TableQuery[ImagesExif]
}
