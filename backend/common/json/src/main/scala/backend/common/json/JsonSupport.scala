package backend.common.json

import spray.json._

import java.time.{Instant, LocalDate}

trait JsonSupport extends DefaultJsonProtocol {

  implicit val instantFormat: RootJsonFormat[Instant] = new RootJsonFormat[Instant] {
    override def write(obj: Instant): JsValue = JsString(obj.toString)

    override def read(json: JsValue): Instant = json match {
      case JsString(value) =>
        Instant.parse(value)
      case value => throw new RuntimeException(s"can not parse $value into Instant, expected a plain ISO Date String")
    }
  }

  implicit val localDateFormat: RootJsonFormat[LocalDate] = new RootJsonFormat[LocalDate] {
    override def write(obj: LocalDate): JsValue = JsString(obj.toString)

    override def read(json: JsValue): LocalDate = json match {
      case JsString(value) => LocalDate.parse(value)
      case value => throw new RuntimeException(s"can not parse $value")
    }
  }

  def jsonFormatIdentityString[T](construct: String => T,
                                  deconstruct: T => String): RootJsonFormat[T] = {
    new RootJsonFormat[T] {
      def write(p: T) = JsString(deconstruct(p))
      def read(value: JsValue) = construct(value.convertTo[String])
    }
  }

}
