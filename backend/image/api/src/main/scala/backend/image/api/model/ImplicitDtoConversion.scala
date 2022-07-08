package backend.image.api.model

import backend.exif.entities.ImageExif

trait ImplicitDtoConversion {

  implicit class ExifToDto(exif: ImageExif) {
    def toDto: ImageExifDto = ImageExifDto(
      exif.cameraMake,
      exif.cameraModel,
      exif.lens,
      exif.focalLength,
      exif.fNumber,
      exif.iso,
      exif.exposureTime,
      exif.date,
      exif.width,
      exif.height
    )
  }

}
