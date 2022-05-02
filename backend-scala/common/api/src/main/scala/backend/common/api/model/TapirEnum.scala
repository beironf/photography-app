package backend.common.api.model

import enumeratum.{Enum, EnumEntry}

trait TapirEnumEntry extends EnumEntry {
  def name: String
  override def entryName: String = name
}

trait TapirEnum[T <: TapirEnumEntry] extends Enum[T] {
  def values: IndexedSeq[T]
  def apply: String => T
}
