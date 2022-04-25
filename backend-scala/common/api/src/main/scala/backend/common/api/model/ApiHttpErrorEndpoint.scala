package backend.common.api.model

import backend.common.api.model.ApiHttpErrors.HttpError
import spray.json.JsonFormat
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.spray.jsonBody

object ApiHttpErrorEndpoint {
  type HttpErrorEndpoint[I, O] = PublicEndpoint[I, HttpError, O, Any]
  type EnvelopedHttpErrorEndpoint[I, O] = HttpErrorEndpoint[I, Enveloped[O]]

  def toJson[T: JsonFormat : Schema]: EndpointIO.Body[String, T] = jsonBody[T]
  def toEnvelopedJson[T: JsonFormat : Schema]: EndpointIO.Body[String, Enveloped[T]] = toJson[Enveloped[T]]
}
