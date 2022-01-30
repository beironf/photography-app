package backend.common.api

import akka.http.scaladsl.server.Route
import backend.core.application.DefaultService

import scala.concurrent.ExecutionContext.Implicits
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait ApiApp extends DefaultService {
  protected implicit lazy val executionContext: ExecutionContext = Implicits.global

  def start(name: String, route: Route, shutdown: Option[() => Future[_]] = None): Unit = {
    ApiStarter.logConfig()
    logger.info(s"Starting $name...")

    ApiStarter.startApi(name, route, shutdown).onComplete {
      case Success(binding) =>
        logger.info(s"Bound to ${binding.localAddress}")
        // DatabaseConnector.startAll() // TODO: Add database?

      case Failure(exception) =>
        logger.error(s"Failed to bind $name, got exception", exception)
    }
  }
}
