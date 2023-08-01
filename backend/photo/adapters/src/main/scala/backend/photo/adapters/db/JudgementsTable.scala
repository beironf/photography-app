package backend.photo.adapters.db

import backend.core.sqlstorage.StorageTableHelper

trait JudgementsTable extends StorageTableHelper {
  import databaseConnector.profile.api.*

  class Judgements(tag: Tag) extends Table[JudgementDb](tag, "judgements") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def rating: Rep[Int] = column[Int]("rating")
    def inShowroom: Rep[Boolean] = column[Boolean]("in_showroom")

    def * = (id, rating, inShowroom) <> (JudgementDb.tupled, JudgementDb.unapply)
  }

  val judgements = TableQuery[Judgements]
}
