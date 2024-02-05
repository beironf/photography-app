package backend.common.api.utils

import backend.common.api.model.ApiHttpErrors.*
import backend.common.api.model.ApiHttpResponse.*
import backend.core.utils.ExceptionFormatter
import backend.core.application.DefaultService

import scala.concurrent.{ExecutionContext, Future}

trait ApiHttpResponseLogger extends DefaultService with ExceptionFormatter {

  implicit class HttpResponseLogger[R](response: Future[HttpResponse[R]]) {
    def logErrors(implicit executionContext: ExecutionContext): Future[Unit] = response
      .map {
        case Left(e) => logHttpError(e)
        case Right(_) => (): Unit
      }
  }

  private def logHttpError(error: HttpError): Unit = error match {
    case e: Unauthorized => logger.info(s"Unauthorized: ${e.message}")
    case e: NotFound => logger.info(s"NotFound: ${e.message}")
    case e: BadRequest => logger.warn(s"BadRequest: ${e.message}")
    case e: Forbidden => logger.warn(s"Forbidden: ${e.message}")
    case e => logger.error(e.message)
  }

  def logCustomException(exception: Exception): Unit =
    logger.warn(s"Domain exception: ${exception.getMessage}")

  def logUnhandledException(exception: Exception): Unit =
    logger.error(s"Internal server error\n${formatException(exception)}")

}
