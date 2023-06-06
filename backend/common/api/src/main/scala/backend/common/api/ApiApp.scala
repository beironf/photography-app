package backend.common.api

import akka.http.scaladsl.server.Route
import backend.core.application.DefaultService
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

trait ApiApp extends ApiStarter with DefaultService {
  protected implicit lazy val executionContext: ExecutionContext = Implicits.global

  def start(name: String, route: Route,
            dbConfigToValidate: Option[DatabaseConfig[JdbcProfile]] = None,
            shutdown: Option[() => Future[_]] = None): Unit = {
    startApi(name, route, shutdown).onComplete {
      case Success(binding) =>
        logger.info(s"Bound to ${binding.localAddress}")
        dbConfigToValidate.foreach(testDatabaseConnection)

      case Failure(exception) =>
        logger.error(s"Failed to bind $name, got exception", exception)
    }
  }

  private def testDatabaseConnection(dbConfig: DatabaseConfig[JdbcProfile]): Unit = {
    val dbHost = dbConfig.config.getString("host")
    val dbPort = dbConfig.config.getInt("port")
    val dbName = dbConfig.config.getString("name")
    val dbUser = dbConfig.config.getString("user")
    logger.info(s"Testing the connection to database=$dbHost:$dbPort/$dbName with user=$dbUser ...")
    Try(dbConfig.db.createSession.conn) match {
      case Success(connection) =>
        logger.info("Connection to database is valid")
        connection.close()
      case _ => logger.error("Connection to database failed")
    }
  }
}
