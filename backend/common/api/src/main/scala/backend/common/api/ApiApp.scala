package backend.common.api

import akka.http.scaladsl.server.Route
import backend.core.application.DefaultService
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

trait ApiApp extends ApiStarter with DefaultService {
  protected implicit lazy val executionContext: ExecutionContext = Implicits.global

  def start(name: String, route: Route,
            dbToValidate: Option[JdbcProfile#Backend#Database] = None,
            shutdown: Option[() => Future[_]] = None): Unit = {
    startApi(name, route, shutdown).onComplete {
      case Success(binding) =>
        logger.info(s"Bound to ${binding.localAddress}")
        dbToValidate.foreach(testDatabaseConnection)

      case Failure(exception) =>
        logger.error(s"Failed to bind $name, got exception", exception)
    }
  }

  private def testDatabaseConnection(db: JdbcProfile#Backend#Database): Unit = {
    logger.info("Testing the database connection ...")
    Try(db.createSession.conn) match {
      case Success(connection) =>
        logger.info("Connection to database is valid")
        connection.close()
      case _ => logger.error("Connection to database failed")
    }
  }
}
