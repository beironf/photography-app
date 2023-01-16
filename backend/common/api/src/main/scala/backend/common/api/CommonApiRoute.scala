package backend.common.api

import akka.http.scaladsl.server.Route
import backend.common.api.model.ApiHttpErrorEndpoint._
import backend.common.api.model.ApiHttpErrors.Unauthorized
import backend.common.api.model.ApiHttpResponse.HttpResponse
import backend.common.api.utils.{ApiHttpResponseLogger, ApiResponseConverter, PasswordHashUtil}
import sttp.capabilities.akka.AkkaStreams
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.{ExecutionContext, Future}
import scala.util.chaining._

trait CommonApiRoute extends ApiHttpResponseLogger with ApiResponseConverter with PasswordHashUtil {
  def route: Route

  val PasswordHash: String = config.getString("admin.api.password-hash")
  val interpreter: AkkaHttpServerInterpreter = AkkaHttpServerInterpreter()

  implicit class ResponseHandler[O](response: Future[HttpResponse[O]]) {
    def handleResponse(implicit executionContext: ExecutionContext): Future[HttpResponse[O]] = response
      .tap(_.logErrors)
  }

  private def authorize(token: AuthenticationToken)
                       (implicit executionContext: ExecutionContext): Future[HttpResponse[Unit]] = Future {
    if (checkPassword(token.value, PasswordHash)) Right((): Unit)
    else Left(Unauthorized("Wrong password"))
  }

  def endpoint[I, O](specification: HttpErrorEndpoint[I, O],
                     implementation: I => Future[HttpResponse[O]])
                    (implicit executionContext: ExecutionContext): Route =
    interpreter.toRoute(
      specification.serverLogic(implementation(_).handleResponse)
    )

  def secureEndpoint[I, O](specification: SecureHttpErrorEndpoint[I, O],
                           implementation: I => Future[HttpResponse[O]])
                    (implicit executionContext: ExecutionContext): Route =
    interpreter.toRoute(
      specification
        .serverSecurityLogic(authorize)
        .serverLogic(_ => implementation(_).handleResponse)
    )

  def streamingAkkaEndpoint[I, O](specification: HttpErrorStreamingEndpoint[I, O, AkkaStreams],
                                  implementation: I => Future[HttpResponse[O]])
                                 (implicit executionContext: ExecutionContext): Route =
    interpreter.toRoute(
      specification.serverLogic(implementation(_).handleResponse)
    )
}
