package backend.photo.entities.meta

case class Metadata(category: Category,
                    peoples: Set[Person],
                    cameraTechnique: CameraTechnique,
                    tags: Set[String])
