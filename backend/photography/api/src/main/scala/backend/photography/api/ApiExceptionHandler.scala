package backend.photography.api

import backend.common.api.utils.ApiExceptionHandling
import backend.common.api.model.ApiHttpErrors.*
import backend.photography.entities.response.Exceptions.*

trait ApiExceptionHandler extends ApiExceptionHandling {
  def specificExceptionHandling: ExceptionToHttpError = {
    case _: PhotoNotFoundException => NotFound("Photo not found")
    case _: PhotoAlreadyExistsException => BadRequest("Photo already exists")
    case _: ImageNotFoundException => NotFound("Image not found")
    case _: ImageAlreadyExistsException => BadRequest("Image already exists")
    case _: ThumbnailNotFoundException => NotFound("Thumbnail not found")
    case _: SiteImageNotFoundException => NotFound("Site image not found")
    case _: ExifNotFoundException => NotFound("Exif not found")
    case _: FileIsNotJPGException => BadRequest("File is not JPG")
    case FileNameNotDefinedException => BadRequest("File name not defined")
  }
}
