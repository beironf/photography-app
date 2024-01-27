import BuildSettings._
import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.Keys._
import sbt.Def
import sbt.Keys._

object ApiDocker {
  val dockerSettings: Seq[Def.SettingsDefinition] = Seq(
    Docker / packageName := name.value,
    dockerBaseImage := "openjdk:11-jdk",
    dockerExposedPorts += 3001,
    dockerEnvVars := Map("PHOTO_APP_DIR" -> "."),
    dockerRepository := Some("europe-west1-docker.pkg.dev/beiron-photography-app/docker-artifacts"),
    dockerUpdateLatest := isMainBuild,
    Docker / defaultLinuxInstallLocation := s"/opt/${name.value}"
  )
}