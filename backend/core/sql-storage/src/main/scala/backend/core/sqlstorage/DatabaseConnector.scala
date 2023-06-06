package backend.core.sqlstorage

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

trait DatabaseConnector {
  val dbConfig: DatabaseConfig[JdbcProfile]
  val db: JdbcProfile#Backend#Database = dbConfig.db
  implicit val executionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
}

object DatabaseConnector {
  val MainDBConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig[JdbcProfile]("database")
}