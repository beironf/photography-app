package backend.photography.interactors.utils

import backend.core.application.DefaultService
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.metadata.{ImageMetadata, OrientationTools}
import com.sksamuel.scrimage.nio.JpegWriter

import java.awt.Rectangle
import java.awt.image.{AreaAveragingScaleFilter, BufferedImage, ColorModel, ImageConsumer}
import java.io.File
import java.util
import javax.imageio.{ImageIO, ImageReader}

object ImageResizer extends DefaultService {

  // A decoded jpeg uses ~3 bytes per pixel. Larger images are decoded and scaled in strips.
  private val MAX_DECODED_PIXELS = 12_000_000L
  private val ROWS_PER_BATCH = 64

  def readDimensions(file: File): (Int, Int) =
    withImageReader(file)(reader => (reader.getWidth(0), reader.getHeight(0)))

  /**
   * Decodes the image once and returns it as jpeg bytes scaled to fit within each of the given sizes.
   * The sizes must be in descending order since each one is scaled from the previous one.
   */
  def resizeImage(file: File, maxSizes: Seq[Int]): Seq[Array[Byte]] = {
    require(maxSizes == maxSizes.sorted.reverse, s"Sizes must be in descending order: $maxSizes")
    logger.info(s"Resizing file")
    val metadata = ImageMetadata.fromFile(file)
    // Each size is scaled from the previous one so that the full size image is never kept in memory
    var image = ImmutableImage.wrapAwt(decode(file, maxSizes.head))
    maxSizes.map { max =>
      image = image.max(max, max)
      OrientationTools.reorient(image, metadata).bytes(JpegWriter.Default)
    }
  }

  private def withImageReader[T](file: File)(f: ImageReader => T): T = {
    val input = ImageIO.createImageInputStream(file)
    try {
      val readers = ImageIO.getImageReaders(input)
      if (!readers.hasNext) throw new IllegalArgumentException(s"No image reader found for file: ${file.getName}")
      val reader = readers.next()
      try {
        reader.setInput(input)
        f(reader)
      } finally reader.dispose()
    } finally input.close()
  }

  /**
   * Decodes the image, already scaled to fit within the max size if it is too large to be decoded at once.
   * The image is then read in strips that are streamed through an area averaging filter.
   */
  private def decode(file: File, maxSize: Int): BufferedImage = withImageReader(file) { reader =>
    val width = reader.getWidth(0)
    val height = reader.getHeight(0)
    if (width.toLong * height <= MAX_DECODED_PIXELS || math.max(width, height) <= maxSize) reader.read(0)
    else {
      val scale = maxSize.toDouble / math.max(width, height)
      val scaled = new BufferedImage(
        math.max(1, math.round(width * scale).toInt),
        math.max(1, math.round(height * scale).toInt),
        BufferedImage.TYPE_INT_RGB)

      val filter = new AreaAveragingScaleFilter(scaled.getWidth, scaled.getHeight).getFilterInstance(new ImageWriter(scaled))
      filter.setDimensions(width, height)
      filter.setColorModel(ColorModel.getRGBdefault)
      filter.setHints(ImageConsumer.TOPDOWNLEFTRIGHT | ImageConsumer.COMPLETESCANLINES | ImageConsumer.SINGLEPASS | ImageConsumer.SINGLEFRAME)

      val rowsPerStrip = math.max(1L, MAX_DECODED_PIXELS / width).toInt
      val pixels = new Array[Int](width * ROWS_PER_BATCH)
      for (y <- 0 until height by rowsPerStrip) {
        val param = reader.getDefaultReadParam
        param.setSourceRegion(new Rectangle(0, y, width, math.min(rowsPerStrip, height - y)))
        val strip = reader.read(0, param)
        for (stripY <- 0 until strip.getHeight by ROWS_PER_BATCH) {
          val rows = math.min(ROWS_PER_BATCH, strip.getHeight - stripY)
          strip.getRGB(0, stripY, width, rows, pixels, 0, width)
          filter.setPixels(0, y + stripY, width, rows, ColorModel.getRGBdefault, pixels, 0, width)
        }
      }
      filter.imageComplete(ImageConsumer.STATICIMAGEDONE)
      scaled
    }
  }

  // Receives the scaled rows from the area averaging filter
  private class ImageWriter(image: BufferedImage) extends ImageConsumer {
    def setPixels(x: Int, y: Int, w: Int, h: Int, model: ColorModel, pixels: Array[Int], off: Int, scansize: Int): Unit =
      image.setRGB(x, y, w, h, pixels, off, scansize)
    def setPixels(x: Int, y: Int, w: Int, h: Int, model: ColorModel, pixels: Array[Byte], off: Int, scansize: Int): Unit =
      throw new UnsupportedOperationException("Only int pixels are expected from the area averaging filter")
    def setDimensions(width: Int, height: Int): Unit = ()
    def setProperties(props: util.Hashtable[?, ?]): Unit = ()
    def setColorModel(model: ColorModel): Unit = ()
    def setHints(hintflags: Int): Unit = ()
    def imageComplete(status: Int): Unit = ()
  }

}
