package backend.core.utils

import java.io.{PrintWriter, StringWriter}

trait ExceptionFormatter {
  def formatException(exception: Exception): String = {
    val sw = new StringWriter()
    exception.printStackTrace(new PrintWriter(sw))
    sw.toString
  }
}
