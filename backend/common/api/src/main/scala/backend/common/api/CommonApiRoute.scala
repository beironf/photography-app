package backend.common.api

import akka.http.scaladsl.server.Route
import backend.common.api.model.ApiHttpErrorEndpoint.{HttpErrorStreamingEndpoint, HttpErrorEndpoint}
import backend.common.api.model.ApiHttpResponse.HttpResponse
import backend.common.api.utils.{ApiHttpResponseLogger, ApiResponseConverter}
import sttp.capabilities.akka.AkkaStreams
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.{ExecutionContext, Future}
import scala.util.chaining._

trait CommonApiRoute extends ApiHttpResponseLogger with ApiResponseConverter {
  def route: Route

  val interpreter: AkkaHttpServerInterpreter = AkkaHttpServerInterpreter()

  implicit class ResponseHandler[O](response: Future[HttpResponse[O]]) {
    def handleResponse(implicit executionContext: ExecutionContext): Future[HttpResponse[O]] = response
      .recover { case e: Exception => Left(e.toHttpError()) }
      .tap(_.logErrors)
  }

  def endpoint[I, O](specification: HttpErrorEndpoint[I, O],
                     implementation: I => Future[HttpResponse[O]])
                    (implicit executionContext: ExecutionContext): Route =
    interpreter.toRoute(specification.serverLogic(implementation(_).handleResponse))

  def streamingAkkaEndpoint[I, O](specification: HttpErrorStreamingEndpoint[I, O, AkkaStreams],
                                  implementation: I => Future[HttpResponse[O]])
                                 (implicit executionContext: ExecutionContext): Route =
    interpreter.toRoute(specification.serverLogic(implementation(_).handleResponse))
}
