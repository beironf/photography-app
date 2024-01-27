package backend.api.model.enums

import backend.common.api.model.{TapirEnum, TapirEnumEntry}

sealed abstract class CameraTechniqueDto(override val name: String) extends TapirEnumEntry

object CameraTechniqueDto extends TapirEnum[CameraTechniqueDto] {
  case object LongExposure extends CameraTechniqueDto("Long Exposure")
  case object Panorama extends CameraTechniqueDto("Panorama")
  case object Aerial extends CameraTechniqueDto("Aerial")
  case object Macro extends CameraTechniqueDto("Macro")
  case object Zooming extends CameraTechniqueDto("Zooming")
  case object Filters extends CameraTechniqueDto("Filters")
  case object MultipleFocusPoints extends CameraTechniqueDto("Multiple Focus Points")

  def values: IndexedSeq[CameraTechniqueDto] = findValues
  def apply: String => CameraTechniqueDto = withName
}
