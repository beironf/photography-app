package backend.exif.adapters.exifdb

import backend.exif.entities.ImageExif

import java.sql.Timestamp

trait ImplicitImageExifConverter {
  implicit class ImageExifDbFromDomain(exif: ImageExif) {
    def toDbFormat(imageId: String): ImageExifDb = ImageExifDb(imageId, exif.cameraMake, exif.cameraModel, exif.lens,
      exif.focalLength, exif.fNumber, exif.iso, exif.exposureTime, exif.date.map(Timestamp.from), exif.width, exif.height)
  }
}
