package backend.photography.adapters.model.photo

import backend.core.sqlstorage.postgres.PostgresTableHelper

trait JudgementsTable extends PostgresTableHelper {
  import databaseConnector.profile.api.*

  class Judgements(tag: Tag) extends Table[JudgementDb](tag, "judgements") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def rating: Rep[Int] = column[Int]("rating")
    def inShowroom: Rep[Boolean] = column[Boolean]("in_showroom")

    def * = (id, rating, inShowroom) <> (JudgementDb.tupled, JudgementDb.unapply)
  }

  val judgements = TableQuery[Judgements]
}
