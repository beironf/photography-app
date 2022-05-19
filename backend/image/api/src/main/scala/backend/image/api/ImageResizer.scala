package backend.image.api

import backend.core.application.DefaultService
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio._

import java.io.File

object ImageResizer extends DefaultService {

  def writerFor(fileName: String): ImageWriter = {
    val ext = fileName.toLowerCase()
    if (ext.endsWith(".png")) PngWriter.MaxCompression
    else if (ext.endsWith(".gif")) GifWriter.Default
    else JpegWriter.Default
  }

  def writerFor(file: File): ImageWriter = writerFor(file.getName)

  def resizeImage(file: File, maxWidth: Int, maxHeight: Int): Array[Byte] = {
    logger.info(s"Resizing file=${file.getName}")
    ImmutableImage.loader()
      .fromFile(file)
      .fit(maxWidth, maxHeight)
      .bytes(writerFor(file.getName))
  }


}
