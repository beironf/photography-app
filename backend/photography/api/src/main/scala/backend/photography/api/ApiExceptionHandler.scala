package backend.photography.api

import backend.common.api.utils.ApiExceptionHandling
import backend.common.api.model.ApiHttpErrors.NotFound
import backend.photography.entities.response.Exceptions.*

trait ApiExceptionHandler extends ApiExceptionHandling {
  def specificExceptionHandling: ExceptionToHttpError = {
    case _: PhotoNotFoundException => NotFound("Photo not found")
  }
}
