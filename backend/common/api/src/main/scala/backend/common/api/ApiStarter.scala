package backend.common.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods.*
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.{Route, RouteConcatenation}
import backend.core.application.DefaultService
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings

import scala.concurrent.duration.*
import scala.concurrent.{Await, Future}

trait ApiStarter extends RouteConcatenation with DefaultService {

  implicit val actorSystem: ActorSystem = ActorSystem("Sys", config)

  private def logConfig(name: String): Unit = {
    val environment = config.getString("environment.name")
    logger.info(s"-------------- STARTING $name IN MODE=$environment --------------")
  }

  def startApi(name: String, route: Route, shutdown: Option[() => Future[?]]): Future[Http.ServerBinding] = {

    val domain: String = config.getString("domain")
    val port: Int = config.getInt(name + ".port")

    logConfig(name)
    logger.info(s"$name starting on $domain:$port")

    val corsSettings = CorsSettings.defaultSettings.withAllowedMethods(Seq(
      GET, POST, PATCH, PUT, DELETE, HEAD, OPTIONS
    ))

    val healthCheckRoute: Route = path("health-check") {
      get {
        complete(OK)
      }
    }

    val bindingFuture = Http().newServerAt(domain, port).bind {
      cors(corsSettings) { healthCheckRoute ~ route }
    }

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
