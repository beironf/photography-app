package backend.photo.adapters.db

import backend.core.sqlstorage.DatabaseConnector

trait LocationsTable extends DatabaseConnector {
  import dbConfig.profile.api._

  class Locations(tag: Tag) extends Table[LocationDb](tag, "locations") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name: Rep[String] = column[String]("name")
    def country: Rep[String] = column[String]("country")
    def longitude: Rep[Float] = column[Float]("longitude")
    def latitude: Rep[Float] = column[Float]("latitude")

    def * = (id, name, country, longitude, latitude) <> (LocationDb.tupled, LocationDb.unapply)
  }

  val locations = TableQuery[Locations]
}
