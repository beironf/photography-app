package backend.core.sqlstorage

import backend.core.application.DefaultService
import com.typesafe.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import slick.jdbc.{JdbcProfile, MySQLProfile}
import slick.util.AsyncExecutor

import java.util.Properties

object DatabaseConnectorMySQL extends DefaultService {
  private def defaultConfig: Config = config.getConfig("database")

  def apply(dbConfig: Config = defaultConfig): DatabaseConnectorMySQL = new DatabaseConnectorMySQL(dbConfig)
}

final class DatabaseConnectorMySQL(val dbConfig: Config) extends DatabaseConnector with DefaultService {
  val profile: JdbcProfile = MySQLProfile

  val dbUser: String = dbConfig.getString("user")

  private val dbName = dbConfig.getString("name")
  private val dbPassword = dbConfig.getString("password")
  private val dbHost = dbConfig.getString("host")
  private val dbPort = dbConfig.getInt("port")

  private val maxPoolSize: Int = dbConfig.getInt(s"max-pool-size")
  private val minIdleSize: Int = dbConfig.getInt(s"min-idle-size")
  private val connectionTimeout = dbConfig.getDuration("connection-timeout")

  private val env = config.getString("environment.name")
  private val isProduction = env == "production"

  val jdbcUrl: String = {
    val host = if (isProduction) {
      ""
    } else {
      s"$dbHost:$dbPort"
    }
    s"jdbc:mysql://$host/$dbName"
  }

  private lazy val hikariDataSource: HikariDataSource = {
    val hikariConfig = new HikariConfig()
    hikariConfig.setJdbcUrl(jdbcUrl)
    hikariConfig.setConnectionTimeout(connectionTimeout.toMillis)
    hikariConfig.setMaximumPoolSize(maxPoolSize)
    hikariConfig.setMinimumIdle(minIdleSize)


    if (isProduction) {
      val cloudSqlInstance = dbHost
      val connProps = new Properties()
      connProps.setProperty("user", dbUser)
      connProps.setProperty("sslmode", "disable")
      connProps.setProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory")
      connProps.setProperty("cloudSqlInstance", cloudSqlInstance)
      connProps.setProperty("enableIamAuth", "true")
      connProps.setProperty("password", "dummy")

      hikariConfig.setDataSourceProperties(connProps)
    } else {
      hikariConfig.setUsername(dbUser)
      hikariConfig.setPassword(dbPassword)
    }

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
