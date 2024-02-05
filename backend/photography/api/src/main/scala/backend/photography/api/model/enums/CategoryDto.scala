package backend.photography.api.model.enums

import backend.common.api.model.{TapirEnum, TapirEnumEntry}

sealed abstract class CategoryDto(override val name: String) extends TapirEnumEntry

object CategoryDto extends TapirEnum[CategoryDto] {
  case object Abstract extends CategoryDto("Abstract")
  case object Animal extends CategoryDto("Animal")
  case object CityAndArchitecture extends CategoryDto("City & Architecture")
  case object Landscape extends CategoryDto("Landscape")
  case object Nature extends CategoryDto("Nature")
  case object Night extends CategoryDto("Night")
  case object People extends CategoryDto("People")

  def values: IndexedSeq[CategoryDto] = findValues
  def apply: String => CategoryDto = withName
}
