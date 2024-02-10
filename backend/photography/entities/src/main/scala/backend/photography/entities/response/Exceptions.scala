package backend.photography.entities.response

object Exceptions {

  sealed class PhotographyException(message: String) extends Exception(message)

  final case class PhotoNotFoundException(imageId: String)
    extends PhotographyException(s"[imageId: $imageId] Photo Not Found")

  final case class PhotoAlreadyExistsException(imageId: String)
    extends PhotographyException(s"[imageId: $imageId] Photo Already Exists")

  final case class ImageNotFoundException(imageId: String)
    extends PhotographyException(s"[imageId: $imageId] Image Not Found")

  final case class ImageAlreadyExistsException(imageId: String)
    extends PhotographyException(s"[imageId: $imageId] Image Already Exists")

  final case class ThumbnailNotFoundException(imageId: String)
    extends PhotographyException(s"[imageId: $imageId] Thumbnail Not Found")

  final case class SiteImageNotFoundException(fileName: String)
    extends PhotographyException(s"[fileName: $fileName] Site Image Not Found")

  final case class ExifNotFoundException(imageId: String)
    extends PhotographyException(s"[imageId: $imageId] Exif Not Found")

  final case class FileIsNotJPGException(fileName: String)
    extends PhotographyException(s"[fileName: $fileName] File is not JPG")

  final case object FileNameNotDefinedException
    extends PhotographyException("File name is not defined")

}
