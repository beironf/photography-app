import sbt.librarymanagement.ConflictWarning

enablePlugins(JavaAppPackaging)

ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.12.8"

conflictWarning := ConflictWarning.disable


// ---------- Project Creator --------

def createProject(id: String, inFile: Option[String] = None)
                 (dependencies: Seq[ModuleID] = Seq.empty): Project =
  Project.apply(id, file(inFile.getOrElse(id)))
    .settings(
      name := id,
      commonSettings,
      libraryDependencies ++= dependencies
    )


// ---------- Domains ----------------

lazy val backend = createProject("backend", inFile = Some("."))()
  .disablePlugins(AssemblyPlugin)
  .aggregate(photoApi)

lazy val core = createProject("core")(Seq(
  dependencies.scalaLogging,
  dependencies.slf4j,
  dependencies.config
))

lazy val common = createProject("common")()

lazy val commonModel = createProject("common-model", inFile = Some("common/model"))()

lazy val commonApi = createProject("common-api", inFile = Some("common/api"))(Seq(
  dependencies.tapirAkkaHttpServer,
  dependencies.tapirCore,
  dependencies.tapirJsonSpray,
  dependencies.tapirOpenApiDocs,
  dependencies.tapirOpenApiCirceYaml
)).dependsOn(commonModel)
  .dependsOn(core)

lazy val photo = createProject("photo")()

lazy val photoApi = createProject("photo-api", inFile = Some("photo/api"))()
  .dependsOn(commonApi)

lazy val photoEntities = createProject("photo-entities", inFile = Some("photo/entities"))()


// ---------- Dependencies -----------

lazy val dependencies = new {
  private val tapirVersion   = "1.0.0-M7"
  private val scalaLoggingV  = "3.9.4"
  private val slf4jV         = "1.7.36"
  private val configV        = "1.4.2"
  private val scalaTestV     = "3.2.11"

  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging"        % scalaLoggingV
  val slf4j        = "org.slf4j"                  % "slf4j-simple"          % slf4jV
  val config    = "com.typesafe" % "config" % configV
  val tapirCore = "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion
  val tapirAkkaHttpServer = "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % tapirVersion
  val tapirJsonSpray = "com.softwaremill.sttp.tapir" %% "tapir-json-spray" % tapirVersion
  val tapirOpenApiDocs = "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"   % tapirVersion
  val tapirOpenApiCirceYaml = "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion
  val scalaTest    = "org.scalatest"              %% "scalatest"            % scalaTestV
}


// ---------- Settings ---------------

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)
