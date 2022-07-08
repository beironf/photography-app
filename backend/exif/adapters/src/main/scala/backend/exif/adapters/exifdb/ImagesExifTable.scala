package backend.exif.adapters.exifdb

import backend.core.sqlstorage.DatabaseConnector

import java.sql.Timestamp

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
    def exposureTime: Rep[Option[String]] = column[Option[String]]("exposure_time")
    def date: Rep[Option[Timestamp]] = column[Option[Timestamp]]("date")
    def width: Rep[Int] = column[Int]("width")
    def height: Rep[Int] = column[Int]("height")

    def * = (imageId, cameraMake, cameraModel, lens, focalLength, fNumber, iso,
      exposureTime, date, width, height) <> (ImageExifDb.tupled, ImageExifDb.unapply)
  }

  val imagesExif = TableQuery[ImagesExif]
}
