package backend.common.api.model

import sttp.tapir.AnyEndpoint

trait EndpointsSpec {
  def endpoints: List[AnyEndpoint]
}
