package backend.common.api

import akka.http.scaladsl.server.Route
import backend.common.api.model.ApiHttpErrorEndpoint.{AkkaStreamsEndpoint, HttpErrorEndpoint}
import backend.common.api.model.ApiHttpResponse.HttpResponse
import backend.common.api.utils.{ApiHttpResponseLogger, ApiResponseConverter}
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.{ExecutionContext, Future}
import scala.util.chaining._

trait CommonApiRoute extends ApiHttpResponseLogger with ApiResponseConverter {
  def route: Route

  val interpreter: AkkaHttpServerInterpreter = AkkaHttpServerInterpreter()

  def endpoint[I, O](specification: HttpErrorEndpoint[I, O],
                     implementation: I => Future[HttpResponse[O]])
                    (implicit executionContext: ExecutionContext): Route =
    interpreter.toRoute(specification.serverLogic(
      implementation(_)
        .recover { case e: Exception => Left(e.toHttpError()) }
        .tap(_.logErrors())
    ))

  def streamingEndpoint[I, O](specification: AkkaStreamsEndpoint[I, O],
                              implementation: I => Future[HttpResponse[O]])
                             (implicit executionContext: ExecutionContext): Route =
    interpreter.toRoute(specification.serverLogic(
      implementation(_)
        .recover { case e: Exception => Left(e.toHttpError()) }
        .tap(_.logErrors())
    ))
}
