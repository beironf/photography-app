package backend.photo.api.model.enums

import backend.common.api.model.{TapirEnum, TapirEnumEntry}

sealed abstract class CameraDto(override val name: String) extends TapirEnumEntry

object CameraDto extends TapirEnum[CameraDto] {
  case object CanonEOS600D extends CameraDto("Canon EOS 600D")
  case object CanonEOS5DMarkII extends CameraDto("Canon EOS 5D Mark II")
  case object FujifilmX100F extends CameraDto("Fujifilm X100F")
  case object DJIMini3 extends CameraDto("DJI Mini 3")

  def values: IndexedSeq[CameraDto] = findValues
  def apply: String => CameraDto = withName
}
