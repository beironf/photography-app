package backend.common.api.utils

import backend.common.api.model.ApiHttpErrors._
import backend.common.api.model.ApiHttpResponse._
import backend.common.model.CommonExceptions._
import backend.core.utils.ThrowableUtil
import backend.core.application.DefaultService

import scala.concurrent.{ExecutionContext, Future}

trait ApiHttpResponseLogger extends DefaultService {

  implicit class HttpResponseLogger[R](response: Future[HttpResponse[R]]) {
    def logErrors()(implicit executionContext: ExecutionContext): Future[Unit] = response
      .map {
        case Left(e) => logHttpError(e)
        case Right(_) => (): Unit
      }
      .recover { e => logException(e) }
  }

  private def logHttpError(error: HttpError): Unit = error match {
    case e: NotFound => logger.info(s"NotFound: ${e.message}")
    case e: BadRequest => logger.warn(s"BadRequest: ${e.message}")
    case e: Forbidden => logger.warn(s"Forbidden: ${e.message}")
    case e => logger.error(e.message)
  }

  private def logException(throwable: Throwable): Unit = throwable match {
    case e: NotFoundException => logger.info(s"NotFoundException: ${e.msg}")
    case e: BadRequestException => logger.warn(s"BadRequestException: ${e.msg}")
    case e: ForbiddenException => logger.warn(s"ForbiddenException: ${e.msg}")
    case e => logger.error(s"Internal server error\n${ThrowableUtil.format(e)}")
  }

}
