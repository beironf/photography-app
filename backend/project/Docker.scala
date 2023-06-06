import BuildSettings._
import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.Keys._
import sbt.Def
import sbt.Keys._

object PhotoApiDocker {
  val dockerSettings: Seq[Def.SettingsDefinition] = Seq(
    Docker / packageName := name.value + "-docker",
    dockerBaseImage := "openjdk:11-jdk",
    dockerExposedPorts += 3001,
    dockerEnvVars := Map("PHOTO_APP_DIR" -> "."),
    dockerRepository := Some("eu.gcr.io"), // TODO: ?
    dockerUsername := Some("beironf"), // TODO: ?
    dockerUpdateLatest := isMainBuild,
    Docker / defaultLinuxInstallLocation := s"/opt/${name.value}"
  )
}

object ImageApiDocker {
  val dockerSettings: Seq[Def.SettingsDefinition] = Seq(
    Docker / packageName := name.value + "-docker",
    dockerBaseImage := "openjdk:11-jdk",
    dockerExposedPorts += 3002,
    dockerEnvVars := Map("PHOTO_APP_DIR" -> "."),
    dockerRepository := Some("eu.gcr.io"), // TODO: ?
    dockerUsername := Some("beironf"), // TODO: ?
    dockerUpdateLatest := isMainBuild,
    Docker / defaultLinuxInstallLocation := s"/opt/${name.value}"
  )
}