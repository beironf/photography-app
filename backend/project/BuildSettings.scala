import scala.language.postfixOps
import scala.sys.process._

// FIXME: Doesn't work with heroku - Can't find a git repository when deploying
object BuildSettings {
  //lazy val gitBranch: String = ("git rev-parse --abbrev-ref HEAD" !!).trim
  //lazy val gitHash: String = ("git rev-parse HEAD" !!).trim
  //lazy val isMainBuild: Boolean = gitBranch == "master"
}