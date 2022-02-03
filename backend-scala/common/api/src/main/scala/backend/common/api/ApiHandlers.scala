package backend.common.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives.{handleExceptions, handleRejections}
import akka.http.scaladsl.server._
import backend.core.application.DefaultService
import backend.common.api.ApiExceptions._
import backend.core.utils.ThrowableUtil

object ApiHandlers extends DefaultService with ApiResponseUtil {

  private def completeWithBadRequest(msg: String): StandardRoute = {
    logger.info(s"BadRequest exception: $msg")
    completeWithErrorCode(BadRequest, s"Bad Request: $msg")
  }

  private def completeWithForbidden(msg: String): StandardRoute = {
    logger.info(s"Forbidden exception: $msg")
    completeWithErrorCode(Forbidden, s"Forbidden: $msg")
  }

  private def completeWithNotFound(msg: String): StandardRoute = {
    logger.info(s"NotFound exception: $msg")
    completeWithErrorCode(NotFound, s"Not Found: $msg")
  }

  private def completeWithInternalError(exc: Throwable): StandardRoute = {
    logger.error(s"Internal server error: \n${ThrowableUtil.format(exc)}")
    completeWithErrorCode(InternalServerError, "Internal Server Error.")
  }


  // must be implicits here for Route.seal to work

  implicit def exceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case BadRequestException(msg) => completeWithBadRequest(msg)
      case ForbiddenException(msg) => completeWithForbidden(msg)
      case NotFoundException(msg) => completeWithNotFound(msg)
      case exc => completeWithInternalError(exc)
    }

  implicit def rejectionHandler: RejectionHandler =
    RejectionHandler.newBuilder()
      .handle { case AuthorizationFailedRejection =>
        completeWithForbidden("Not Authorized")
      }
      .handleAll[AuthenticationFailedRejection] { _ =>
        completeWithErrorCode(Unauthorized, s"Not authenticated.")
      }
      .handle { case MalformedRequestContentRejection(_, e) =>
        completeWithBadRequest(s"Unable to serialize to JSON: ${e.getMessage}.")
      }
      .handle { case MalformedQueryParamRejection(paramName, msg, _) =>
        completeWithBadRequest(s"Invalid value supplied for '$paramName': $msg")
      }
      .handle { case MissingQueryParamRejection(paramName) =>
        completeWithBadRequest(s"Missing parameter: '$paramName''")
      }
      .handle { case MissingHeaderRejection(paramName) =>
        completeWithBadRequest(s"Missing header: '$paramName''")
      }
      .handle { case MissingFormFieldRejection(paramName) =>
        completeWithBadRequest(s"Missing form field: '$paramName''")
      }
      .handleAll[MethodRejection] { methodRejections =>
        val names = methodRejections.map(_.supported.name)
        completeWithErrorCode(MethodNotAllowed, s"Unsupported HTTP method! This route handles: " +
          s"${names mkString " or "}! [This is a catch-all exception]")
      }
      .handleNotFound {
        completeWithNotFound("These are not the droids you're looking for.")
      }
      .result() withFallback RejectionHandler.default

  lazy val handleErrors: Directive[Unit] = handleRejections(rejectionHandler) & handleExceptions(exceptionHandler)
}
