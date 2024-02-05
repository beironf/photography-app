package backend.photography.api.model

import backend.photography.api.model.enums.{CameraTechniqueDto, CategoryDto}

case class MetadataDto(category: CategoryDto,
                       cameraTechniques: Set[CameraTechniqueDto],
                       tags: Set[String])
