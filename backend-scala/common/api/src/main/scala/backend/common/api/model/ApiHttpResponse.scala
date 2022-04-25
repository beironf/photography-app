package backend.common.api.model

import backend.common.api.model.ApiHttpErrors.HttpError

object ApiHttpResponse {
  type HttpResponse[R] = Either[HttpError, R]
  type EnvelopedHttpResponse[R] = HttpResponse[Enveloped[R]]
}
