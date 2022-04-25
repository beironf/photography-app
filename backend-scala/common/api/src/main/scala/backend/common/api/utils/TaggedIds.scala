package backend.common.api.utils

import backend.core.utils.TaggedType.{@@, Tag}
import sttp.tapir.CodecFormat.TextPlain
import sttp.tapir.{Codec, Schema}

import java.util.UUID

object TaggedIds {
  implicit def anyTaggedLongCodec[G]: Codec[String, Long @@ G, TextPlain] =
    Codec.long.map(Tag[Long, G])(_.asInstanceOf[Long])

  implicit def anyTaggedUUIDCodec[G]: Codec[String, UUID @@ G, TextPlain] =
    Codec.uuid.map(Tag[UUID, G])(_.asInstanceOf[UUID])

  implicit def anyTaggedStringCodec[G]: Codec[String, String @@ G, TextPlain] =
    Codec.string.map(Tag[String, G])(_.asInstanceOf[String])


  implicit def anyTaggedLongSchema[G]: Schema[Long @@ G] = Schema.schemaForLong.as[Long @@ G]

  implicit def anyTaggedUUIDSchema[G]: Schema[UUID @@ G] = Schema.schemaForUUID.as[UUID @@ G]

  implicit def anyTaggedStringSchema[G]: Schema[String @@ G] = Schema.schemaForString.as[String @@ G]
}
