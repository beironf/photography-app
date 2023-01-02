package backend.image.api

import backend.exif.entities.ImageExif
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.metadata.ImageMetadata

import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


object ExifUtil {

  private val CAMERA_MAKE = 271
  private val CAMERA_MODEL = 272
  private val LENS_MODEL = 42036
  private val FOCAL_LENGTH = 37386
  private val F_NUMBER = 33437
  private val ISO = 34855
  private val EXPOSURE_TIME = 33434
  private val DATE = 36867

  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")
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

    val image = ImmutableImage.loader().fromFile(file)
    val width = image.width
    val height = image.height

    ImageExif(cameraMake, cameraModel, lens, focalLength, fNumber, iso, exposureTime, date, width, height)
  }

}
