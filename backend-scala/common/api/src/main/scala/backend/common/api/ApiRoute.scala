package backend.common.api

import akka.http.scaladsl.server.Route
import backend.core.application.DefaultService

trait ApiRoute extends DefaultService with ApiJsonSupport with ApiResponseUtil {
  def route: Route
}
