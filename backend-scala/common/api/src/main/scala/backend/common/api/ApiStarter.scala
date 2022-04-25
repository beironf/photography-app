package backend.common.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Route, RouteConcatenation}
import backend.core.application.DefaultService

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object ApiStarter extends RouteConcatenation with DefaultService {

  implicit val actorSystem: ActorSystem = ActorSystem("API Actor System", config)

  private def logConfig(name: String): Unit = {
    val environment = config.getString("environment.name")
    logger.info(s"-------------- STARTING $name IN MODE=$environment --------------")
  }

  def startApi(name: String, route: Route, shutdown: Option[() => Future[_]]): Future[Http.ServerBinding] = {

    val domain: String = config.getString("domain")
    val port: Int = config.getInt(name + ".port")

    logConfig(name)
    logger.info(s"$name starting on $domain:$port")

    val bindingFuture = Http().newServerAt(domain, port).bind(route)

    sys.addShutdownHook {
      logger.info("Received shutdown signal, stopping http server")
      val onceAllConnectionsTerminated: Future[Http.HttpTerminated] =
        Await.result(bindingFuture, 30.seconds).terminate(hardDeadline = 1.minute)
      Await.ready(onceAllConnectionsTerminated, 10.seconds)

      logger.info("Server terminated, running any defined shutdown function")
      val shutdownFuture = shutdown.map(shutdownFunction => shutdownFunction()).getOrElse(Future.unit)
      Await.ready(shutdownFuture, 30.seconds)

      logger.info("Shutdown functions terminated, stopping actor system")
      actorSystem.terminate()
    }

    bindingFuture
  }
}
