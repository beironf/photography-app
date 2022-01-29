package backend.core.application

import akka.actor.{ActorSystem, Scheduler}
import akka.stream.Materializer

import scala.concurrent.ExecutionContextExecutor

/**
 * This should be the default actor system
 */
object SystemActorSystem extends DefaultService {
  lazy val actorSystem: ActorSystem = ActorSystem("Sys", config)
  lazy val materializer: Materializer = Materializer.matFromSystem(actorSystem)

  object Implicits {
    implicit lazy val actorSystem: ActorSystem = SystemActorSystem.actorSystem
    implicit lazy val materializer: Materializer = SystemActorSystem.materializer
    implicit lazy val executor: ExecutionContextExecutor = actorSystem.dispatcher
    implicit lazy val scheduler: Scheduler = actorSystem.scheduler
  }
}
