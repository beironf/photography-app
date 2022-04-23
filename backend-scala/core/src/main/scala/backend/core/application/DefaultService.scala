package backend.core.application

import com.typesafe.scalalogging.StrictLogging
import com.typesafe.config.{Config => TypesafeConfig}

// a default class to keep most classes even cleaner
trait DefaultService extends StrictLogging {
  lazy val config: TypesafeConfig = Config()
}
