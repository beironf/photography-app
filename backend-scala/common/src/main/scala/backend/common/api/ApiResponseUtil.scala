package backend.common.api

import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.server.StandardRoute
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import com.annotell.common.baseapi.model.ResponseMessage
import com.annotell.common.baseapi.model.ResponseJsonProtocol.responseMessageFormat
import com.annotell.common.utils.ValidationModel.ValidationError
import spray.json._

import scala.reflect.runtime.universe._

object ApiResponseUtil {

  import ApiJsonSupport._

  def completeWithErrorCode(statusCode: StatusCode, message: String): StandardRoute =
    complete(statusCode -> Map("message" -> message.toJson))

  def completeEnveloped[T: JsonFormat](statusCode: StatusCode, serializableClass: T): StandardRoute =
    complete(statusCode -> Map("data" -> serializableClass.toJson))

  def completeEnveloped[T: JsonFormat](statusCode: StatusCode, serializableClasses: Seq[T]): StandardRoute =
    complete(statusCode -> Map("data" -> serializableClasses.toJson))

}
