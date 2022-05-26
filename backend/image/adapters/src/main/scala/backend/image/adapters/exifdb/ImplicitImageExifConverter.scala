package backend.image.adapters.exifdb

import backend.image.entities.ImageExif

trait ImplicitImageExifConverter {
  implicit class ImageExifDbFromDomain(exif: ImageExif) {
    def toDbFormat(imageId: String): ImageExifDb = ImageExifDb(imageId, exif.cameraMake, exif.cameraModel, exif.lens,
      exif.focalLength, exif.fNumber, exif.iso, exif.exposureTime, exif.date)
  }
}
