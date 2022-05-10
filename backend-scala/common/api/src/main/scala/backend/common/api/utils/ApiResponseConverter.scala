package backend.common.api.utils

import backend.common.api.model.ApiHttpErrors._
import backend.common.api.model.ApiHttpResponse._
import backend.common.api.model.Enveloped
import backend.common.model.CommonExceptions._
import backend.core.application.DefaultService
import backend.core.utils.ThrowableUtil

import scala.concurrent.{ExecutionContext, Future}

trait ApiResponseConverter extends DefaultService {

  implicit class ToEnveloped[R](response: Future[Either[HttpError, R]]) {
    def toEnveloped(implicit executionContext: ExecutionContext): Future[EnvelopedHttpResponse[R]] =
      response.map(_.map(Enveloped(_)))
  }

  implicit class FromFutureEither[R](response: Future[Either[Throwable, R]]) {
    def toHttpResponse(implicit executionContext: ExecutionContext): Future[HttpResponse[R]] =
      mapResponse(response)

    def toEnvelopedHttpResponse(implicit executionContext: ExecutionContext): Future[EnvelopedHttpResponse[R]] =
      mapResponse(response.map(_.map(Enveloped(_))))

    private def mapResponse[T](response: Future[Either[Throwable, T]])
                              (implicit executionContext: ExecutionContext): Future[HttpResponse[T]] =
      response.map {
        case Right(r) => Right(r)
        case Left(e) => Left(e.toHttpError)
      }
  }

  implicit class FromFuture[R](response: Future[R]) {
    def toHttpResponse(implicit executionContext: ExecutionContext): Future[HttpResponse[R]] =
      response
        .map(Right(_))
        .recover(Left(_))
        .toHttpResponse

    def toEnvelopedHttpResponse(implicit executionContext: ExecutionContext): Future[EnvelopedHttpResponse[R]] =
      response
        .map(Right(_))
        .recover(Left(_))
        .toEnvelopedHttpResponse
  }

  implicit class ExceptionToHttpError(throwable: Throwable) {
    def toHttpError: HttpError = throwable match {
      case e: NotFoundException => NotFound(e.msg)
      case e: BadRequestException => BadRequest(e.msg)
      case e: ForbiddenException => Forbidden(e.msg)
      case e =>
        // We don't want the internal message in the http-response, log here before it's removed
        logger.error(s"Internal server error\n${ThrowableUtil.format(e)}")
        InternalServerError()
    }
  }

}
