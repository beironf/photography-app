package backend.core.application

import com.typesafe.scalalogging.StrictLogging
import com.typesafe.config.{Config => TypesafeConfig}

trait DefaultService extends StrictLogging {
  lazy val config: TypesafeConfig = Config()
}
