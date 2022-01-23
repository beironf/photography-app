package backend.core.application

import com.typesafe.config.{ConfigFactory, Config => TypesafeConfig}
import com.typesafe.scalalogging.StrictLogging

/**
 * Using typesafe config under the hood.
 * https://github.com/lightbend/config
 * Set the java property -Dconfig.resource=$myenv.conf or
 * Set the java property -Dconfig.file=my/file.conf
 */

// the config is essentially singleton, and only needs to be read once.
object Config extends StrictLogging {
  val DEVELOPMENT = "development"
  val PRODUCTION = "production"

  def apply(env: String = DEVELOPMENT): TypesafeConfig = {
    env match {
      case DEVELOPMENT => developmentConfig
      case PRODUCTION => productionConfig
      case name => load(name)
    }
  }

  def load(name: String) = {
    val configFile = s"$name.conf"
    ConfigFactory.load(configFile)
  }

  lazy val developmentConfig = ConfigFactory.load("application.conf")
  lazy val productionConfig = ConfigFactory.load("production.conf")
}