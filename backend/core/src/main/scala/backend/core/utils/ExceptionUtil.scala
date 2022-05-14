package backend.core.utils

import java.io.{PrintWriter, StringWriter}

object ExceptionUtil {
  def format(exception: Exception): String = {
    val sw = new StringWriter()
    exception.printStackTrace(new PrintWriter(sw))
    sw.toString
  }
}
