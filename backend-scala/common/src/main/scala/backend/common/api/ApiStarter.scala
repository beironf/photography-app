package backend.common.api

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Route, RouteConcatenation}
import backend.core.application.{DefaultService, SystemActorSystem}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object ApiStarter extends RouteConcatenation with DefaultService {

  def logConfig(): Unit = {
    val environment = config.getString("environment.name")
    logger.info(s"-------------- STARTING API IN MODE=$environment --------------")
  }

  def startApi(name: String, route: Route, shutdown: Option[() => Future[_]]): Future[Http.ServerBinding] = {
    import SystemActorSystem.Implicits._

    val host = "0.0.0.0"
    val port: Int = config.getInt(name + ".port")

    logger.info(s"$name starting on $host:$port")

    val total = ApiHandlers.handleErrors {
      route
    }

    val binding = Http().newServerAt(host, port).bind(total)

    sys.addShutdownHook {
      logger.info("Received shutdown signal, stopping http server")
      val onceAllConnectionsTerminated: Future[Http.HttpTerminated] =
        Await.result(binding, 30.seconds).terminate(hardDeadline = 1.minute)
      Await.ready(onceAllConnectionsTerminated, 10.seconds)

      logger.info("Server terminated, running any defined shutdown function")
      val shutdownFuture = shutdown.map(shutdownFunction => shutdownFunction()).getOrElse(Future.unit)
      Await.ready(shutdownFuture, 30.seconds)

      logger.info("Shutdown functions terminated, stopping actor system")
      actorSystem.terminate()
    }

    binding
  }
}
