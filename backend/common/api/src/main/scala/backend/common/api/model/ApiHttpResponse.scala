package backend.common.api.model

import backend.common.api.model.ApiHttpErrors.HttpError

object ApiHttpResponse {
  type HttpResponse[O] = Either[HttpError, O]
  type EnvelopedHttpResponse[O] = HttpResponse[Enveloped[O]]
}
