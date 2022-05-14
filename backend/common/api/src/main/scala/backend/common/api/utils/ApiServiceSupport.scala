package backend.common.api.utils

import backend.core.utils.EitherTExtensions

trait ApiServiceSupport extends ApiHttpResponseLogger
  with ApiResponseConverter
  with EitherTExtensions
