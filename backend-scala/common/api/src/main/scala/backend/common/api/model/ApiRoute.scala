package backend.common.api.model

import akka.http.scaladsl.server.Route
import backend.core.application.DefaultService

trait ApiRoute extends DefaultService {
  def route: Route
}
