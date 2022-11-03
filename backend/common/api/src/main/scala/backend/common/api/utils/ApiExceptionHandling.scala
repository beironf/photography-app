package backend.common.api.utils

import backend.common.api.model.ApiHttpErrors._
import backend.common.model.CommonExceptions._

trait ApiExceptionHandling extends ApiHttpResponseLogger {

  type ExceptionToHttpError = PartialFunction[Exception, HttpError]

  implicit class ExceptionHandling(exception: Exception) {
    def toHttpError(customExceptionConverter: ExceptionToHttpError = PartialFunction.empty): HttpError = {
      if (customExceptionConverter.isDefinedAt(exception))  {
        logCustomException(exception)
        customExceptionConverter(exception)
      } else {
        logCommonException(exception)
        commonExceptionConverter(exception)
      }
    }
  }

  private def commonExceptionConverter(exception: Exception): HttpError = exception match {
    case e: NotFoundException => NotFound(e.msg)
    case e: BadRequestException => BadRequest(e.msg)
    case e: ForbiddenException => Forbidden(e.msg)
    case _ => InternalServerError()
  }

}
