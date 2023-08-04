package backend.image.adapters.model

case class FileId(bucket: String,
                  path: String) {
  require(!path.contains(".."))

  private lazy val parts = path.split("/").toVector
  lazy val dir = parts.dropRight(1).mkString("/")
  lazy val topDir = parts.head
  lazy val fileName = parts.last

  lazy val StorageProviderBasePath = "gs://"

  lazy val toResourceId: String = StorageProviderBasePath + s"$bucket/$path"

  override def toString: String = toResourceId
}
