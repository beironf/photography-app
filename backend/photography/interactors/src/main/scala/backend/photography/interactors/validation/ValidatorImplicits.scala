package backend.photography.interactors.validation

import backend.photography.entities.response.Response.PhotographyResponse

trait ValidatorImplicits {

  implicit class PhotographyResponseToBoolean[O](response: PhotographyResponse[O]) {
    def toBoolean: Boolean = response match {
      case Right(_) => true
      case Left(_) => false
    }
  }

}
