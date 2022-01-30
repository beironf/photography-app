package backend.common.json

import scalaz.{@@, Tag}

import java.util.UUID

object Conversions {

  implicit class TaggedIdToLong[T](taggedId: Long @@ T) {
    def untag: Long = Tag.unwrap(taggedId)
  }

  implicit class TaggedOptionIdToOptionLong[T](taggedIdOpt: Option[Long @@ T]) {
    def untag: Option[Long] = taggedIdOpt.map(taggedId => Tag.unwrap(taggedId))
  }

  implicit class TaggedUUIDToUUID[T](taggedUUID: UUID @@ T) {
    def untag: UUID = Tag.unwrap(taggedUUID)
  }

}
