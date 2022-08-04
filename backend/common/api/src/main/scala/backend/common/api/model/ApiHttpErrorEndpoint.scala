package backend.common.api.model

import backend.common.api.model.ApiHttpErrors.HttpError
import spray.json.JsonFormat
import sttp.capabilities.Streams
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.spray.jsonBody

object ApiHttpErrorEndpoint {
  type HttpErrorEndpoint[I, O] = PublicEndpoint[I, HttpError, O, Any]
  type EnvelopedHttpErrorEndpoint[I, O] = HttpErrorEndpoint[I, Enveloped[O]]
  type HttpErrorStreamingEndpoint[I, O, R <: Streams[_]] = PublicEndpoint[I, HttpError, O, R]

  def toJson[T: JsonFormat : Schema]: EndpointIO.Body[String, T] = jsonBody[T]
  def toEnvelopedJson[T: JsonFormat : Schema]: EndpointIO.Body[String, Enveloped[T]] = toJson[Enveloped[T]]
}
