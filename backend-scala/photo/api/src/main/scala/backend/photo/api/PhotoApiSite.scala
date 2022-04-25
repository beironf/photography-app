package backend.photo.api

import akka.Done
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import backend.photo.api.routes.PhotoRoute

import scala.concurrent.{ExecutionContext, Future}

class PhotoApiSite (implicit executionContext: ExecutionContext) {

  def healthCheckRoute: Route = path("health-check") {
    // TODO: Check if API is ready, DB connection etc.
    get { complete(NoContent) }
  }

  private val photoRoute = new PhotoRoute()

  val route: Route = {
    healthCheckRoute ~
    pathPrefix("v1") {
      photoRoute.route
    }
  }

  def shutdown(): Future[Done] = Future.successful(Done)
}
