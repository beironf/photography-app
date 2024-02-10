package backend.photography.interactors

import backend.core.application.DefaultService
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.*

import java.io.File

object ImageResizer extends DefaultService {

  private def writerFor(fileName: String): ImageWriter = {
    val ext = fileName.toLowerCase()
    if (ext.endsWith(".png")) PngWriter.MaxCompression
    else if (ext.endsWith(".gif")) GifWriter.Default
    else JpegWriter.Default
  }

  def resizeImage(file: File, max: Int): Array[Byte] = {
    logger.info(s"Resizing file")
    ImmutableImage.loader()
      .fromFile(file)
      .max(max, max)
      .bytes(writerFor(file.getName))
  }


}
