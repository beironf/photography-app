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
    def toHttpResponse(additionalExceptionHandling: Option[ExceptionToHttpError] = None)
                      (implicit executionContext: ExecutionContext): Future[HttpResponse[R]] =
      mapResponse(response, additionalExceptionHandling)

    def toEnvelopedHttpResponse(additionalExceptionHandling: Option[ExceptionToHttpError] = None)
                               (implicit executionContext: ExecutionContext): Future[EnvelopedHttpResponse[R]] =
      mapResponse(response.map(_.map(Enveloped(_))), additionalExceptionHandling)

    private def mapResponse[T](response: Future[Either[Exception, T]],
                              additionalExceptionHandling: Option[ExceptionToHttpError] = None)
                              (implicit executionContext: ExecutionContext): Future[HttpResponse[T]] =
      response.map {
        case Right(r) => Right(r)
        case Left(e) => Left(e.toHttpError(additionalExceptionHandling))
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
