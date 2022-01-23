package backend.core.application

import com.typesafe.scalalogging.StrictLogging

// a default class to keep most classes even cleaner
trait DefaultService extends StrictLogging {
  lazy val config = Config()
}
