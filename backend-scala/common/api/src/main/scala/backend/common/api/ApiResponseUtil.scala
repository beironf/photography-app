package backend.common.api

import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.server.StandardRoute
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import spray.json._

object ApiResponseUtil {

  import ApiJsonSupport._

  def completeWithErrorCode(statusCode: StatusCode, message: String): StandardRoute =
    complete(statusCode -> Map("message" -> message.toJson))

  def completeEnveloped[T: JsonFormat](statusCode: StatusCode, serializableClass: T): StandardRoute =
    complete(statusCode -> Map("data" -> serializableClass.toJson))

  def completeEnveloped[T: JsonFormat](statusCode: StatusCode, serializableClasses: Seq[T]): StandardRoute =
    complete(statusCode -> Map("data" -> serializableClasses.toJson))

  case class ResponseMessage(status: Long, message: String)

  implicit def responseMessageFormat: RootJsonFormat[ResponseMessage] = jsonFormat2(ResponseMessage.apply)

}
