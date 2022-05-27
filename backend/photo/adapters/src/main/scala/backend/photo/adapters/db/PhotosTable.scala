package backend.photo.adapters.db

import backend.core.sqlstorage.DatabaseConnector

trait PhotosTable extends DatabaseConnector {

  import dbConfig.profile.api._

  class Photos(tag: Tag) extends Table[PhotoDb](tag, "photos") {

    def imageId: Rep[String] = column[String]("image_id", O.PrimaryKey)
    def title: Rep[String] = column[String]("title")

    def * = (imageId, title) <> (PhotoDb.tupled, PhotoDb.unapply)
  }

  val photos = TableQuery[Photos]
}
