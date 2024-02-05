package backend.photography.adapters.model.photo

import backend.photography.entities.photo.meta.Judgement

case class JudgementDb(id: Long,
                       rating: Int,
                       inShowroom: Boolean) {
  def toDomain: Judgement = Judgement(rating, inShowroom)
}
