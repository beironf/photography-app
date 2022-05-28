package backend.photo.api.model.dtos

import backend.photo.api.model.enums._
import backend.photo.entities.meta._

case class MetadataDto(category: CategoryDto,
                       cameraTechnique: CameraTechniqueDto,
                       tags: Set[String])
