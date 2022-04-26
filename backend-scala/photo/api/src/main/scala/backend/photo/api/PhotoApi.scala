package backend.photo.api

import backend.common.api.ApiApp

object PhotoApi extends App with ApiApp {
  val site = new PhotoApiSite()
  start("photo-api", site.route, Some(() => site.shutdown()))
}
