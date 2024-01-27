package backend.core.sqlstorage

import backend.core.sqlstorage.postgres.DatabaseConnectorPostgres
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

trait DatabaseConnector {
  val profile: JdbcProfile

  val db: profile.api.Database

  val jdbcUrl: String

  val dbUser: String

  val dbPassword: String

  def close(): Unit
}

object DatabaseConnector {
  implicit val executionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  lazy val defaultPostgresConnector: DatabaseConnectorPostgres = DatabaseConnectorPostgres()
}