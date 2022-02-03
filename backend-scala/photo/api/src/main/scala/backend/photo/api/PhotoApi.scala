package backend.photo.api

import akka.Done
import backend.common.api.ApiApp
import scala.concurrent.Future

object PhotoApi extends App with ApiApp {
  val site = new PhotoApiSite()
  def shutdown(): Future[Done] = site.shutdown()

  start("photo-api", site.route, Some(shutdown))
}
