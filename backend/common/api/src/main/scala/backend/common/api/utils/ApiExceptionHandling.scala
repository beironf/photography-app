package backend.common.api.utils

import backend.common.api.model.ApiHttpErrors.*

trait ApiExceptionHandling extends ApiHttpResponseLogger {

  type ExceptionToHttpError = PartialFunction[Exception, HttpError]

  implicit class ExceptionHandling(exception: Exception) {
    def toHttpError(customExceptionConverter: ExceptionToHttpError = PartialFunction.empty): HttpError = {
      if (customExceptionConverter.isDefinedAt(exception))  {
        logCustomException(exception)
        customExceptionConverter(exception)
      } else {
        logUnhandledException(exception)
        InternalServerError()
      }
    }
  }

}
