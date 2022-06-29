package backend.photo.adapters.db

import backend.photo.entities.meta.Judgement

case class JudgementDb(id: Long,
                       rating: Int,
                       inShowroom: Boolean) {
  def toDomain: Judgement = Judgement(rating, inShowroom)
}
