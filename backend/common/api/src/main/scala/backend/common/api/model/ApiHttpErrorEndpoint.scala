package backend.common.api.model

import backend.common.api.model.ApiHttpErrors.HttpError
import sttp.capabilities.Streams
import sttp.tapir._

object ApiHttpErrorEndpoint {
  type HttpErrorEndpoint[I, O] = PublicEndpoint[I, HttpError, O, Any]
  type EnvelopedHttpErrorEndpoint[I, O] = HttpErrorEndpoint[I, Enveloped[O]]
  type SecureHttpErrorEndpoint[I, O] = Endpoint[AuthenticationToken, I, HttpError, O, Any]
  type HttpErrorStreamingEndpoint[I, O, R <: Streams[_]] = PublicEndpoint[I, HttpError, O, R]

  case class AuthenticationToken(value: String)

  implicit class HttpErrorEndpointImplicits[I, O](endpoint: HttpErrorEndpoint[I, O]) {
    def appendPasswordProtection: SecureHttpErrorEndpoint[I, O] =
      endpoint.securityIn(auth.bearer[String]().mapTo[AuthenticationToken])
  }
}
