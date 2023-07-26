package backend.core.application

import com.typesafe.config.{ConfigFactory, Config as TypesafeConfig}

/**
 * Using typesafe config under the hood.
 * https://github.com/lightbend/config
 * Set the java property -Dconfig.resource=production.conf to enable production conf
 */

// the config is essentially singleton, and only needs to be read once.
object Config {
  private val PRODUCTION = "production"

  private val isProduction = Option(System.getProperty("config.resource"))
    .exists(_.contains(PRODUCTION))

  def apply(): TypesafeConfig =
    if (isProduction) productionConfig
    else developmentConfig

  private lazy val developmentConfig: TypesafeConfig = ConfigFactory.load("application.conf")
  private lazy val productionConfig: TypesafeConfig = ConfigFactory.load("production.conf")
}
