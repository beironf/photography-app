package backend.common.api.utils

import org.mindrot.jbcrypt.BCrypt

trait PasswordHashUtil {

  def checkPassword(plainText: String, hashedPassword: String): Boolean =
    hashedPassword.nonEmpty && BCrypt.checkpw(plainText, hashedPassword)

}
