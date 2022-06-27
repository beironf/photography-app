package backend.photo.entities.meta

case class Metadata(category: Category.Value,
                    cameraTechniques: Set[CameraTechnique.Value],
                    tags: Set[String])
