package backend.core.sqlstorage.postgres

import backend.core.sqlstorage.DatabaseConnector

import scala.concurrent.Future

object PostgresMaterializerDBIO {
  def apply(databaseConnector: DatabaseConnectorPostgres = DatabaseConnector.defaultPostgresConnector): PostgresMaterializerDBIO =
    new PostgresMaterializerDBIO(databaseConnector)
}

class PostgresMaterializerDBIO(val databaseConnector: DatabaseConnectorPostgres) extends PostgresTableHelper {

  import databaseConnector.*
  import databaseConnector.profile.api.*

  def run[R](op: DBIO[R]): Future[R] = db.run(op)
  def runTransactionally[R](op: DBIO[R]): Future[R] = db.run(op.transactionally)
}
