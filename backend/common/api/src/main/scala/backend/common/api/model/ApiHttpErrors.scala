package backend.common.api.model

import spray.json.DefaultJsonProtocol.*
import spray.json.RootJsonFormat

object ApiHttpErrors {
  sealed trait HttpError {
    def message: String
  }

  final case class Unauthorized(message: String = "Unauthorized") extends HttpError
  final case class Forbidden(message: String = "Forbidden") extends HttpError
  final case class BadRequest(message: String = "Bad request") extends HttpError
  final case class NotFound(message: String = "Not found") extends HttpError
  final case class InternalServerError(message: String = "Internal server error") extends HttpError

  object JsonProtocol {
    implicit def unauthorizedFormat: RootJsonFormat[Unauthorized] = jsonFormat1(Unauthorized)
    implicit def badRequestFormat: RootJsonFormat[BadRequest] = jsonFormat1(BadRequest)
    implicit def forbiddenFormat: RootJsonFormat[Forbidden] = jsonFormat1(Forbidden)
    implicit def notFoundFormat: RootJsonFormat[NotFound] = jsonFormat1(NotFound)
    implicit def internalServerErrorFormat: RootJsonFormat[InternalServerError] = jsonFormat1(InternalServerError)
  }
}
