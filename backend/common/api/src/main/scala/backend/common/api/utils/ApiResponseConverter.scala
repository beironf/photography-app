package backend.common.api.utils

import backend.common.api.model.ApiHttpErrors._
import backend.common.api.model.ApiHttpResponse._
import backend.common.api.model.Enveloped

import scala.concurrent.{ExecutionContext, Future}

trait ApiResponseConverter extends ApiExceptionHandling {

  implicit class ToEnveloped[R](response: Future[Either[HttpError, R]]) {
    def toEnveloped(implicit executionContext: ExecutionContext): Future[EnvelopedHttpResponse[R]] =
      response.map(_.map(Enveloped(_)))
  }

  implicit class FromFutureEither[R](response: Future[Either[Exception, R]]) {
    def toHttpResponse(customExceptionConverter: ExceptionToHttpError = PartialFunction.empty)
                      (implicit executionContext: ExecutionContext): Future[HttpResponse[R]] =
      mapResponse(response, customExceptionConverter)

    def toEnvelopedHttpResponse(customExceptionConverter: ExceptionToHttpError = PartialFunction.empty)
                               (implicit executionContext: ExecutionContext): Future[EnvelopedHttpResponse[R]] =
      mapResponse(response.map(_.map(Enveloped(_))), customExceptionConverter)

    private def mapResponse[T](response: Future[Either[Exception, T]],
                               customExceptionConverter: ExceptionToHttpError)
                              (implicit executionContext: ExecutionContext): Future[HttpResponse[T]] =
      response.map {
        case Right(r) => Right(r)
        case Left(e) => Left(e.toHttpError(customExceptionConverter))
      }.recover {
        case e: Exception => Left(e.toHttpError(customExceptionConverter))
      }
  }

  implicit class FromFuture[R](response: Future[R]) {
    def toHttpResponse(implicit executionContext: ExecutionContext): Future[HttpResponse[R]] =
      response
        .map(Right(_))
        .toHttpResponse()

    def toEnvelopedHttpResponse(implicit executionContext: ExecutionContext): Future[EnvelopedHttpResponse[R]] =
      response
        .map(Right(_))
        .toEnvelopedHttpResponse()
  }

}
