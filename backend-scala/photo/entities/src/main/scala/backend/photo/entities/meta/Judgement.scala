package backend.photo.entities.meta

case class Judgement(rating: Int,
                     inShowroom: Boolean) {
  require(rating >= 0 && rating <= 10)
}
