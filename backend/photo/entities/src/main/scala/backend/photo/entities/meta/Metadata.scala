package backend.photo.entities.meta

case class Metadata(category: Category.Value,
                    cameraTechnique: CameraTechnique.Value,
                    tags: Set[String])
