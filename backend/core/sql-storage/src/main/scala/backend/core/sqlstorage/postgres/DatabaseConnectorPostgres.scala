package backend.core.sqlstorage.postgres

import backend.core.application.DefaultService
import backend.core.sqlstorage.DatabaseConnector
import com.typesafe.config.Config
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import slick.util.AsyncExecutor

object DatabaseConnectorPostgres extends DefaultService {
  private def defaultConfig: Config = config.getConfig("database")

  def apply(dbConfig: Config = defaultConfig): DatabaseConnectorPostgres = new DatabaseConnectorPostgres(dbConfig)
}

final class DatabaseConnectorPostgres(val dbConfig: Config) extends DatabaseConnector with DefaultService {
  val profile: MyPostgresProfile = MyPostgresProfile

  val jdbcUrl: String = dbConfig.getString("url")
  val dbUser: String = dbConfig.getString("user")
  val dbPassword: String = dbConfig.getString("password")

  private val maxPoolSize: Int = dbConfig.getInt(s"max-pool-size")
  private val minIdleSize: Int = dbConfig.getInt(s"min-idle-size")
  private val connectionTimeout = dbConfig.getDuration("connection-timeout")

  private lazy val hikariDataSource: HikariDataSource = {
    val hikariConfig = new HikariConfig()
    hikariConfig.setJdbcUrl(jdbcUrl.split('?').head) // remove user and password from url
    hikariConfig.setUsername(dbUser)
    hikariConfig.setPassword(dbPassword)
    hikariConfig.setConnectionTimeout(connectionTimeout.toMillis)
    hikariConfig.setMaximumPoolSize(maxPoolSize)
    hikariConfig.setMinimumIdle(minIdleSize)
    new HikariDataSource(hikariConfig)
  }

  private def createDatabaseDef(): profile.backend.DatabaseDef = {
    val executor = AsyncExecutor(
      name = "DbThreadPoolExecutor",
      minThreads = maxPoolSize,
      maxThreads = maxPoolSize,
      queueSize = 1000,
      maxConnections = maxPoolSize
    )
    profile.api.Database.forDataSource(hikariDataSource,
      maxConnections = Some(maxPoolSize),
      executor = executor
    )
  }

  lazy val db: profile.api.Database = {
    logger.info(s"[databaseUrl: $jdbcUrl, databaseUser: $dbUser, maxPoolSize=$maxPoolSize] Connecting to database")
    createDatabaseDef()
  }

  override def close(): Unit = {
    hikariDataSource.close()
    db.close()
  }
}
