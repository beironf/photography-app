package backend.api.model

import backend.api.model.enums.{CameraTechniqueDto, CategoryDto}

case class MetadataDto(category: CategoryDto,
                       cameraTechniques: Set[CameraTechniqueDto],
                       tags: Set[String])
