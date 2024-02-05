package backend.photography.entities.response

import backend.photography.entities.response.Exceptions.PhotographyException

object Response {
  type PhotographyResponse[O] = Either[PhotographyException, O]
}
