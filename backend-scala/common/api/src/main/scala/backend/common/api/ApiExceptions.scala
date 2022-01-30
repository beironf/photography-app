package backend.common.api

object ApiExceptions {
  final case class BadRequestException(msg: String) extends Exception(msg) // 400
  final case class ForbiddenException(msg: String) extends Exception(msg)  // 403
  final case class NotFoundException(msg: String) extends Exception(msg)   // 404

  implicit class BooleanThrowException(isOk: Boolean) {
    def throwIfNotTrue(ifFalse: Exception): Unit = if (isOk) (): Unit else throw ifFalse
  }
}
