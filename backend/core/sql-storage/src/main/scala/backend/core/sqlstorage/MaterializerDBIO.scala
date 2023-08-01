package backend.core.sqlstorage

import scala.concurrent.Future

object MaterializerDBIO {
  def apply(databaseConnector: DatabaseConnector = DatabaseConnector.defaultConnector): MaterializerDBIO =
    new MaterializerDBIO(databaseConnector)
}

class MaterializerDBIO(val databaseConnector: DatabaseConnector) extends StorageTableHelper {

  import databaseConnector.*
  import databaseConnector.profile.api.*

  def run[R](op: DBIO[R]): Future[R] = db.run(op)
  def runTransactionally[R](op: DBIO[R]): Future[R] = db.run(op.transactionally)
}
