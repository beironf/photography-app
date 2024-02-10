package backend.photography.repositories.model.image

case class FileId(bucket: String,
                  path: String) {
  require(!path.contains(".."))

  private lazy val parts = path.split("/").toVector
  lazy val dir: String = parts.dropRight(1).mkString("/")
  lazy val topDir: String = parts.head
  lazy val fileName: String = parts.last

  private lazy val StorageProviderBasePath: String = "gs://"
  private lazy val toResourceId: String = StorageProviderBasePath + s"$bucket/$path"

  override def toString: String = toResourceId
}
