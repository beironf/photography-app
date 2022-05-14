package backend.common.api.model

import spray.json.{DefaultJsonProtocol, JsonFormat, RootJsonFormat}

case class Enveloped[D](data: D)

case object Enveloped extends DefaultJsonProtocol {
  implicit def jsonFormat[D: JsonFormat]: RootJsonFormat[Enveloped[D]] = jsonFormat1(Enveloped[D])
}
