package backend.common.api.utils

import backend.common.api.model.ApiHttpErrors._
import backend.common.api.model.ApiHttpResponse._
import backend.common.api.model.Enveloped
import backend.common.model.CommonExceptions._
import backend.core.utils.ThrowableUtil
import backend.core.application.DefaultService

import scala.concurrent.{ExecutionContext, Future}

trait ApiExceptionsHandler extends DefaultService {

  implicit class ImplicitConverter[R](routeResponse: Future[R]) {
    def toHttpResponse(implicit executionContext: ExecutionContext): Future[HttpResponse[R]] =
      mapResponse(routeResponse)

    def toEnvelopedHttpResponse(implicit executionContext: ExecutionContext): Future[EnvelopedHttpResponse[R]] =
      mapResponse(routeResponse.map(Enveloped(_)))

    private def mapResponse[T](response: Future[T])
                              (implicit executionContext: ExecutionContext): Future[HttpResponse[T]] =
      response
        .map(Right(_))
        .recover {
          case e: NotFoundException =>
            logger.info(s"NotFound exception: ${e.msg}")
            Left(NotFound())
          case e: BadRequestException =>
            logger.warn(s"BadRequest exception: ${e.msg}")
            Left(BadRequest())
          case e: ForbiddenException =>
            logger.warn(s"Forbidden exception: $e.msg")
            Left(Forbidden())
          case e: Throwable =>
            logger.error(s"Internal server error\n${ThrowableUtil.format(e)}")
            Left(InternalServerError())
        }
  }
}
