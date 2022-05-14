package backend.photo.api.model.enums

import backend.common.api.model.{TapirEnum, TapirEnumEntry}

sealed abstract class LensDto(override val name: String) extends TapirEnumEntry

object LensDto extends TapirEnum[LensDto] {
  case object Sigma10_20 extends LensDto("Sigma 10-20mm f/4-5.6 EX DC HSM")
  case object Canon50F1_8 extends LensDto("Canon 50mm f:1.8")
  case object Canon70_300 extends LensDto("Canon 70-300mm f:4-5.6")
  case object Canon24_70L extends LensDto("Canon 24-70mm 2.8 L")
  case object Fujinon35 extends LensDto("Fujinon 35mm f:2")

  def values: IndexedSeq[LensDto] = findValues
  def apply: String => LensDto = withName
}
