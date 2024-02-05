package backend.photography.entities.photo.meta

import CameraTechnique.CameraTechnique
import Category.Category

case class Metadata(category: Category,
                    cameraTechniques: Set[CameraTechnique],
                    tags: Set[String])
