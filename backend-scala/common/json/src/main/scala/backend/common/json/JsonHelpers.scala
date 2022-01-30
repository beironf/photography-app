package backend.common.json

import spray.json.{JsString, JsValue, RootJsonFormat}
import spray.json.DefaultJsonProtocol._

object JsonHelpers {

  def enumJsonFormat[T](construct: String => T,
                        deconstruct: T => String): RootJsonFormat[T] = {
    new RootJsonFormat[T] {
      def write(p: T): JsString = JsString(deconstruct(p))
      def read(value: JsValue): T = construct(value.convertTo[String])
    }
  }

}
