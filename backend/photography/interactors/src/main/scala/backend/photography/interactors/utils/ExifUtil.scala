package backend.photography.interactors.utils

import backend.photography.entities.exif.ImageExif
import com.sksamuel.scrimage.metadata.ImageMetadata

import java.io.File
import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId}

object ExifUtil {

  private val CAMERA_MAKE = 271
  private val CAMERA_MODEL = 272
  private val LENS_MODEL = 42036
  private val FOCAL_LENGTH = 37386
  private val F_NUMBER = 33437
  private val ISO = 34855
  private val EXPOSURE_TIME = 33434
  private val DATE = 36867
  private val ORIENTATION = 274

  private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")
    .withZone(ZoneId.of("Europe/Stockholm"))

  def getExif(file: File): ImageExif = {
    val tags = ImageMetadata.fromFile(file).tags()

    def getValue[T](`type`: Int,
                    mapper: String => T = (s: String) => s.asInstanceOf[T],
                    useRawValue: Boolean = false): Option[T] =
      tags
        .find(_.getType == `type`)
        .map(tag => mapper(if (useRawValue) tag.getRawValue else tag.getValue))

    val cameraMake = getValue(CAMERA_MAKE)
    val cameraModel = getValue(CAMERA_MODEL)
    val lens = getValue(LENS_MODEL)
    val focalLength = getValue(FOCAL_LENGTH, _.toFloat, useRawValue = true)
    val fNumber = getValue(F_NUMBER, _.toFloat, useRawValue = true)
    val iso = getValue(ISO, _.toInt)
    val exposureTime = getValue(EXPOSURE_TIME, useRawValue = true)
    val date = getValue(DATE, s => Instant.from(formatter.parse(s)))

    // read from the file header to avoid decoding the image, swapped if the image is rotated 90 degrees
    val (rawWidth, rawHeight) = ImageResizer.readDimensions(file)
    val isRotated = getValue(ORIENTATION, _.toIntOption, useRawValue = true).flatten.exists(o => o >= 5 && o <= 8)
    val (width, height) = if (isRotated) (rawHeight, rawWidth) else (rawWidth, rawHeight)

    ImageExif(cameraMake, cameraModel, lens, focalLength, fNumber, iso, exposureTime, date, width, height)
  }

}
