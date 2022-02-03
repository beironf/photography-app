package backend.common.api.model

import spray.json.DefaultJsonProtocol._
import spray.json.{JsValue, RootJsonFormat}

case class ResponseMessage(status: Long, content: Map[String, JsValue])

object ResponseMessageJsonProtocol {
  implicit val responseMessageFormat: RootJsonFormat[ResponseMessage] = jsonFormat2(ResponseMessage.apply)
}
