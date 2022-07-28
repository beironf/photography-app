package backend.photo.entities.meta

import backend.photo.entities.meta.CameraTechnique.CameraTechnique
import backend.photo.entities.meta.Category.Category

case class Metadata(category: Category,
                    cameraTechniques: Set[CameraTechnique],
                    tags: Set[String])
