package backend.photo.api

import backend.common.api.model.ApiHttpResponse.EnvelopedHttpResponse
import backend.common.api.utils.ApiExceptionsHandler.ImplicitConverter

import scala.concurrent.{ExecutionContext, Future}

class ApiService(implicit executionContext: ExecutionContext) {

  def getPhoto(photoId: String): Future[EnvelopedHttpResponse[Int]] = // TODO: Photo response type
    Future.successful(1)
      .toEnvelopedHttpResponse
}
