package backend.photography.entities.image

import akka.stream.IOResult
import akka.stream.scaladsl.Source
import akka.util.ByteString

import scala.concurrent.Future

trait ImageIO {
  type ImageStream = Source[ByteString, Future[IOResult]]

  case class UploadResult(bytes: Long = 0)
}
