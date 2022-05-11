package backend.common.api.utils

import backend.common.api.model.ApiHttpErrors._
import backend.common.model.CommonExceptions._

trait ApiExceptionHandling extends ApiHttpResponseLogger {

  type ExceptionToHttpError = PartialFunction[Exception, HttpError]

  implicit class ExceptionHandling(exception: Exception) {
    def toHttpError(additionalExceptionHandling: Option[ExceptionToHttpError] = None): HttpError =
      commonExceptionHandling.lift(exception)
        .getOrElse(additionalExceptionHandling.flatMap(_.lift(exception))
          .getOrElse(internalServerHttpError(exception)))
  }

  private def commonExceptionHandling: PartialFunction[Exception, HttpError] = {
    case e: NotFoundException => NotFound(e.msg)
    case e: BadRequestException => BadRequest(e.msg)
    case e: ForbiddenException => Forbidden(e.msg)
  }

  private val internalServerHttpError = (e: Exception) => {
    // We don't want the internal message in the http-response, log here before it's removed
    logException(e)
    InternalServerError()
  }

}
