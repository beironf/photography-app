package backend.api.model.enums

import backend.common.api.model.{TapirEnum, TapirEnumEntry}

sealed abstract class LensDto(override val name: String) extends TapirEnumEntry

object LensDto extends TapirEnum[LensDto] {
  case object Sigma10_20 extends LensDto("Sigma 10-20mm f/4-5.6 EX DC HSM")
  case object Canon50F1_8 extends LensDto("EF50mm f/1.8 II")
  case object Canon70_300 extends LensDto("EF70-300mm f/4-5.6 IS USM")
  case object Canon24_70L extends LensDto("EF24-70mm f/2.8L USM")
  case object Fujinon35 extends LensDto("Fujinon 35mm f:2")
  case object Fujinon16 extends LensDto("Fujinon 16mm f/1.4 R WR")
  case object Fujinon90 extends LensDto("Fujinon 90mm f/2 R LM WR")
  case object DJI6_7F1_7 extends LensDto("DJI 6.7mm f/1.7")

  def values: IndexedSeq[LensDto] = findValues
  def apply: String => LensDto = withName
}
