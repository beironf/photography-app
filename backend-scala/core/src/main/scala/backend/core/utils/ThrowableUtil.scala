package backend.core.utils

import java.io.{PrintWriter, StringWriter}

object ThrowableUtil {
  def format(throwable: Throwable): String = {
    val sw = new StringWriter()
    throwable.printStackTrace(new PrintWriter(sw))
    sw.toString
  }
}
