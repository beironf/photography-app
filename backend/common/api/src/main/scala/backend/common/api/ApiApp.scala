package backend.common.api

import akka.http.scaladsl.server.Route
import backend.core.application.DefaultService
import backend.core.sqlstorage.DatabaseConnector

import scala.concurrent.ExecutionContext.Implicits
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

trait ApiApp extends ApiStarter with DefaultService {
  protected implicit lazy val executionContext: ExecutionContext = Implicits.global

  def start(name: String,
            route: Route,
            dbConnectorToValidate: Option[DatabaseConnector] = None,
            shutdown: Option[() => Future[?]] = None): Unit = {
    startApi(name, route, shutdown).onComplete {
      case Success(binding) =>
        logger.info(s"Bound to ${binding.localAddress}")
        dbConnectorToValidate.foreach(testDatabaseConnection)

      case Failure(exception) =>
        logger.error(s"Failed to bind $name, got exception", exception)
    }
  }

  private def testDatabaseConnection(dbConnector: DatabaseConnector): Unit =
    Try(dbConnector.db.createSession().conn) match {
      case Success(connection) =>
        logger.info("Connection to database is valid")
        connection.close()
      case Failure(e) => logger.error(s"Connection to database failed: ${e.getMessage}")
    }
}
