package backend.photography.entities.response

object Exceptions {

  sealed class PhotographyException(message: String) extends Exception(message)

  final case class PhotoNotFoundException(imageId: String)
    extends PhotographyException(s"[imageId: $imageId] Photo Not Found")

}
