package backend.common.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives.{as, entity, provide}
import akka.http.scaladsl.unmarshalling.Unmarshaller.{longFromStringUnmarshaller, uuidFromStringUnmarshaller}
import akka.http.scaladsl.unmarshalling.{FromRequestUnmarshaller, Unmarshaller}
import backend.common.json.BaseJsonSupport

import java.sql.Timestamp
import java.time.{Instant, LocalDate}
import java.util.UUID

object ApiJsonSupport extends ApiJsonSupport

trait ApiJsonSupport extends BaseJsonSupport with SprayJsonSupport {

  /*
   * Makes it possible to directly transform ID's in GET calls to taggedIds.
   */
  import scalaz.{@@, Tag}
  implicit def taggedIdLong[G]: Unmarshaller[String, Long @@ G] = longFromStringUnmarshaller.map(v => Tag[Long, G](v))
  implicit def taggedString[G]: Unmarshaller[String, String @@ G] = Unmarshaller.strict(v => Tag[String, G](v))
  implicit def taggedUUID[G]: Unmarshaller[String, UUID @@ G] = uuidFromStringUnmarshaller.map(v => Tag[UUID, G](v))

  def optionalEntity[T](unmarshaller: FromRequestUnmarshaller[T]): Directive1[Option[T]] = {
    entity(as[String]).flatMap { stringEntity =>
      if(stringEntity == null || stringEntity.isEmpty) {
        provide(Option.empty[T])
      } else {
        entity(unmarshaller).flatMap(e => provide(Some(e)))
      }
    }
  }

  /*
    * Makes it possible to directly transform strings in GET calls to java.time.LocalDate
    */
  implicit val stringToLocalDateUnmarshaller: Unmarshaller[String, LocalDate] = Unmarshaller.strict[String, LocalDate] {
    v => LocalDate.parse(v)
  }

  /*
    * Makes it possible to directly transform strings in GET calls to java.time.Instant
    */
  implicit val stringToInstantUnmarshaller: Unmarshaller[String, Instant] = Unmarshaller.strict[String, Instant] {
    v => {
      val isOldFormat = !v.contains("Z")
      if (isOldFormat) {
        Timestamp.valueOf(v).toInstant
      } else Instant.parse(v)
    }
  }
}
