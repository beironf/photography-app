package backend.common.json

import scalaz.{@@, Tag}
import spray.json._

import java.time.{Instant, LocalDate}
import java.util.UUID
import scala.util.Try
import Conversions._

trait BaseJsonSupport extends DefaultJsonProtocol {

  implicit val instantFormat: RootJsonFormat[Instant] = new RootJsonFormat[Instant] {
    override def write(obj: Instant): JsValue = JsString(obj.toString)

    override def read(json: JsValue): Instant = json match {
      case JsString(value) => Instant.parse(value)
      case value => deserializationError(s"can not parse $value into Instant")
    }
  }

  implicit val localDateFormat: RootJsonFormat[LocalDate] = new RootJsonFormat[LocalDate] {
    override def write(obj: LocalDate): JsValue = JsString(obj.toString)

    override def read(json: JsValue): LocalDate = json match {
      case JsString(value) => LocalDate.parse(value)
      case value => deserializationError(s"can not parse $value into LocalDate")
    }
  }

  implicit val uuidFormat: RootJsonFormat[UUID] = new RootJsonFormat[UUID] {
    override def write(obj: UUID): JsValue = JsString(obj.toString)

    override def read(json: JsValue): UUID = json match {
      case JsString(uuid) => Try(UUID.fromString(uuid)).getOrElse(deserializationError("Expected UUID format"))
      case _ => deserializationError("Expected UUID format")
    }
  }

  def taggedMapKeysToString[G, T](taggedMap: Map[Long @@ G, T]): Map[String, T] = {
    taggedMap.map{ case (k, v) => k.untag.toString -> v }
  }

  implicit def anyTaggedTypeJsonFormat[T: JsonFormat, G]: JsonFormat[T @@ G] = {
    new RootJsonFormat[T @@ G] {
      def read(json: JsValue): T @@ G = Tag[T, G](json.convertTo[T])

      def write(obj: T @@ G): JsValue = Tag.unwrap(obj).toJson

      def write(obj: Option[T @@ G]): JsValue =
        obj.map(i => Tag.unwrap(i).toJson).getOrElse(JsNull)
    }
  }
}

object BaseJsonSupport extends BaseJsonSupport
