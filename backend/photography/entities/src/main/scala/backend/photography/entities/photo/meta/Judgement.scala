package backend.photography.entities.photo.meta

case class Judgement(rating: Int,
                     inShowroom: Boolean) {
  require(rating >= 0 && rating <= 10)
}
