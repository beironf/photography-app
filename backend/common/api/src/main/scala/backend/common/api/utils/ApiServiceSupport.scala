package backend.common.api.utils

import backend.core.utils.EitherTExtensions

trait ApiServiceSupport extends ApiResponseConverter
  with EitherTExtensions
