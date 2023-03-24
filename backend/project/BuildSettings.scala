import scala.language.postfixOps
import scala.sys.process._

object BuildSettings {
  lazy val timestamp: String = ("git show -s --format=%cd --date=format-local:%Y%m%dT%H%M" !!).trim
  lazy val gitBranch: String = ("git rev-parse --abbrev-ref HEAD" !!).trim
  lazy val gitHash: String = ("git rev-parse HEAD" !!).trim
  lazy val isMainBuild: Boolean = gitBranch == "master"
}