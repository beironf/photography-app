package backend.common.api.model

import backend.common.api.model.ApiHttpErrors.HttpError
import sttp.capabilities.Streams
import sttp.tapir._

object ApiHttpErrorEndpoint {
  type HttpErrorEndpoint[I, O] = PublicEndpoint[I, HttpError, O, Any]
  type EnvelopedHttpErrorEndpoint[I, O] = HttpErrorEndpoint[I, Enveloped[O]]
  type HttpErrorStreamingEndpoint[I, O, R <: Streams[_]] = PublicEndpoint[I, HttpError, O, R]
}
