package backend.common.api.utils

import backend.common.api.model.ApiHttpErrors.*
import backend.common.api.model.ApiHttpErrors.JsonProtocol.*
import sttp.model.StatusCode
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.spray.*

object ApiHttpErrorsHandler {
  val commonErrorsOut: EndpointOutput.OneOf[HttpError, HttpError] = {
    oneOf[HttpError](
      oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[NotFound].description("Not found"))),
      oneOfVariant(statusCode(StatusCode.Unauthorized).and(jsonBody[Unauthorized].description("Unauthorized"))),
      oneOfVariant(statusCode(StatusCode.Forbidden).and(jsonBody[Unauthorized].description("Forbidden"))),
      oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[BadRequest].description("Bad request"))),
      oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[InternalServerError].description("Internal server error"))),
    )
  }
}
