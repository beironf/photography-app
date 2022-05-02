package backend.photo.api.model

import backend.photo.entities.meta.Category
import enumeratum.{Enum, EnumEntry}

sealed abstract class CategoryDto(val name: String) extends EnumEntry

object CategoryDto extends Enum[CategoryDto] {

  def values: IndexedSeq[CategoryDto] = findValues
  case object Abstract extends CategoryDto("Abstract")
  case object Animal extends CategoryDto("Animal")
  case object CityAndArchitecture extends CategoryDto("City & Architecture")
  case object Landscape extends CategoryDto("Landscape")
  case object Nature extends CategoryDto("Nature")
  case object Night extends CategoryDto("Night")
  case object People extends CategoryDto("People")

  def apply(name: String): CategoryDto = CategoryDto.withName(name)

}
