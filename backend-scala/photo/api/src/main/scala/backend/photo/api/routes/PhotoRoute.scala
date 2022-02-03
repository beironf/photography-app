package backend.photo.api.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import backend.common.api.ApiRoute
import backend.common.api.pathmatchers.TaggedPathMatcher._

import scala.concurrent.{ExecutionContext, Future}

class PhotoRoute()(implicit executionContext: ExecutionContext) extends ApiRoute {

  val route: Route = pathPrefix("photos") {
    listPhotos
  }

  def listPhotos: Route = get {
    onSuccess(Future.unit) {
      completeWithNoContent
    }
  }
}