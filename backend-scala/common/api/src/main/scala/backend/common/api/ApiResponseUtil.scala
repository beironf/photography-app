package backend.common.api

import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.model.StatusCodes.NoContent
import akka.http.scaladsl.server.StandardRoute
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import backend.common.api.model.ResponseMessage
import backend.common.api.model.ResponseMessageJsonProtocol._
import spray.json._

trait ApiResponseUtil {

  import ApiJsonSupport._

  def completeWithNoContent: StandardRoute = complete(NoContent)

  def completeWithErrorCode(statusCode: StatusCode, message: String): StandardRoute =
    complete(ResponseMessage(statusCode.intValue, Map("message" -> message.toJson)))

  def completeEnveloped[T: JsonFormat](statusCode: StatusCode, serializableClass: T): StandardRoute =
    complete(ResponseMessage(statusCode.intValue, Map("data" -> serializableClass.toJson)))

  def completeEnveloped[T: JsonFormat](statusCode: StatusCode, serializableClasses: Seq[T]): StandardRoute =
    complete(ResponseMessage(statusCode.intValue, Map("data" -> serializableClasses.toJson)))

}
