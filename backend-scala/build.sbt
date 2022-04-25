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
  dependencies.akkaSlf4j
))

lazy val common = createProject("common")()

lazy val commonJson = createProject("common-json", inFile = Some("common/json"))(Seq(
  dependencies.scalaz,
  dependencies.tapirJsonSpray
))

lazy val commonApi = createProject("common-api", inFile = Some("common/api"))(Seq(
  dependencies.tapirAkkaHttpServer,
  dependencies.tapirCore,
  dependencies.tapirJsonSpray,
  dependencies.tapirOpenApiDocs,
  dependencies.tapirOpenApiCirceYaml
)).dependsOn(common)
  .dependsOn(core)
  .dependsOn(commonJson)

lazy val photo = createProject("photo")()

lazy val photoApi = createProject("photo-api", inFile = Some("photo/api"))()
  .dependsOn(commonApi)

lazy val photoEntities = createProject("photo-entities", inFile = Some("photo/entities"))()


// ---------- Dependencies -----------

lazy val dependencies = new {
  private val akkaHttpV      = "10.2.7"
  private val swaggerAkkaHttpVersion = "2.7.0"
  private val akkaV          = "2.6.18"
  private val tapirVersion   = "1.0.0-M7"
  private val sprayJsonV     = "1.3.6"
  private val scalaTestV     = "3.2.10"
  private val scalaLoggingV  = "3.9.4"
  private val scalazV        = "7.3.6"
  private val slf4jV         = "1.7.36"

  //val sprayJson    = "io.spray"                   %% "spray-json"           % sprayJsonV
  //val akkaHttp     = "com.typesafe.akka"          %% "akka-http"            % akkaHttpV
  //val akkaHttpJson = "com.typesafe.akka"          %% "akka-http-spray-json" % akkaHttpV
  //val akkaActor    = "com.typesafe.akka"          %% "akka-actor"           % akkaV
  //val akkaStream   = "com.typesafe.akka"          %% "akka-stream"          % akkaV
  val akkaSlf4j    = "com.typesafe.akka"          %% "akka-slf4j"           % akkaV
  val akkaTestkit  = "com.typesafe.akka"          %% "akka-testkit"         % akkaV
  val scalaz       = "org.scalaz"                 %% "scalaz-core"          % scalazV
  val scalaTest    = "org.scalatest"              %% "scalatest"            % scalaTestV
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging"        % scalaLoggingV
  val slf4j        = "org.slf4j"                  % "slf4j-simple"          % slf4jV
  val tapirCore = "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion
  val tapirAkkaHttpServer = "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % tapirVersion
  val tapirJsonSpray = "com.softwaremill.sttp.tapir" %% "tapir-json-spray" % tapirVersion
  val tapirOpenApiDocs = "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"   % tapirVersion
  val tapirOpenApiCirceYaml = "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion
  //val swaggerAkkaHttp = "com.github.swagger-akka-http" %% "swagger-akka-http" % swaggerAkkaHttpVersion
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
