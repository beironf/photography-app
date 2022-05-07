lazy val commonSettings = Seq(
  organization := "com.beironf",
  scalaVersion := "2.13.8",
  version := "1.0"
)


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
  .aggregate(photoApi)

lazy val core = createProject("core")(Seq(
  dependencies.scalaLogging,
  dependencies.slf4j,
  dependencies.config
))


// ---- Common

lazy val common = createProject("common")()

lazy val commonModel = createProject("common-model", inFile = Some("common/model"))()

lazy val commonJson = createProject("common-json", inFile = Some("common/json"))(Seq(
  dependencies.tapirJsonSpray
))

lazy val commonApi = createProject("common-api", inFile = Some("common/api"))(Seq(
  dependencies.tapirAkkaHttpServer,
  dependencies.tapirCore,
  dependencies.tapirOpenApiDocs,
  dependencies.tapirOpenApiCirceYaml,
  dependencies.tapirEnumeratum
)).dependsOn(commonModel)
  .dependsOn(commonJson)
  .dependsOn(core)


// ---- Photo

lazy val photo = createProject("photo")()

lazy val photoApi = createProject("photo-api", inFile = Some("photo/api"))()
  .dependsOn(commonApi)
  .dependsOn(photoInteractors)
  .dependsOn(photoAdapters)

lazy val photoEntities = createProject("photo-entities", inFile = Some("photo/entities"))()

lazy val photoPorts = createProject("photo-ports", inFile = Some("photo/ports"))()
  .dependsOn(photoEntities)

lazy val photoAdapters = createProject("photo-adapters", inFile = Some("photo/adapters"))()
  .dependsOn(photoPorts)

lazy val photoInteractors = createProject("photo-interactors", inFile = Some("photo/interactors"))()
  .dependsOn(photoPorts)


// ---- Image

lazy val image = createProject("image")()

lazy val imageApi = createProject("image-api", inFile = Some("image/api"))()
  .dependsOn(commonApi)
  .dependsOn(imageInteractors)
  .dependsOn(imageAdapters)

lazy val imageEntities = createProject("image-entities", inFile = Some("image/entities"))(Seq(
  dependencies.akkaStream
))

lazy val imagePorts = createProject("image-ports", inFile = Some("image/ports"))()
  .dependsOn(imageEntities)

lazy val imageAdapters = createProject("image-adapters", inFile = Some("image/adapters"))()
  .dependsOn(imagePorts)
  .dependsOn(core)

lazy val imageInteractors = createProject("image-interactors", inFile = Some("image/interactors"))()
  .dependsOn(imagePorts)


// ---------- Dependencies -----------

lazy val dependencies = new {
  private val akkaV         = "2.6.19"
  private val tapirV        = "1.0.0-M7"
  private val scalaLoggingV = "3.9.4"
  private val slf4jV        = "1.7.36"
  private val configV       = "1.4.2"
  private val scalaTestV    = "3.2.11"

  val scalaLogging          = "com.typesafe.scala-logging"  %% "scala-logging"            % scalaLoggingV
  val akkaStream            = "com.typesafe.akka"           %% "akka-stream"              % akkaV
  val slf4j                 = "org.slf4j"                   %  "slf4j-simple"             % slf4jV
  val config                = "com.typesafe"                %  "config"                   % configV
  val tapirCore             = "com.softwaremill.sttp.tapir" %% "tapir-core"               % tapirV
  val tapirAkkaHttpServer   = "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server"   % tapirV
  val tapirJsonSpray        = "com.softwaremill.sttp.tapir" %% "tapir-json-spray"         % tapirV
  val tapirOpenApiDocs      = "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % tapirV
  val tapirOpenApiCirceYaml = "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirV
  val tapirEnumeratum       = "com.softwaremill.sttp.tapir" %% "tapir-enumeratum"         % tapirV
  val scalaTest             = "org.scalatest"               %% "scalatest"                % scalaTestV
}
