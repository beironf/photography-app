package backend.common.api

import akka.http.scaladsl.server.Route
import backend.common.api.model.ApiHttpErrorEndpoint.HttpErrorEndpoint
import backend.common.api.model.ApiHttpResponse.HttpResponse
import backend.core.application.DefaultService
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.Future

trait CommonApiRoute extends DefaultService {
  def route: Route

  val interpreter: AkkaHttpServerInterpreter = AkkaHttpServerInterpreter()

  def tapirRoute[I, O](specification: HttpErrorEndpoint[I, O],
                       implementation: I => Future[HttpResponse[O]]): Route =
    interpreter.toRoute(specification.serverLogic(implementation))
}
