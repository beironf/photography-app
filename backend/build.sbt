import Dependencies._
import BuildSettings._

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / organization := "com.beironf"
ThisBuild / version := "1.0.0"//s"$gitHash"

ThisBuild / scalacOptions ++= Seq(
  "-Xsource:3",
  "-feature",
  "-language:implicitConversions",
  "-Wunused:imports",
  "-Yrangepos"
)


// ---------- Project Creator --------

def createProject(id: String, inFile: Option[String] = None)
                 (dependencies: Seq[ModuleID] = Seq.empty): Project =
  Project.apply(id, file(inFile.getOrElse(id)))
    .settings(
      name := id,
      libraryDependencies ++= dependencies
    )


// ---------- Domains ----------------

lazy val backend = createProject("backend", inFile = Some("."))()
  .aggregate(api)


// ---- Core

lazy val core = createProject("core")(Seq(
  scalaLogging,
  slf4j,
  scalaConfig,
  catsCore
))

lazy val coreSqlStorage = createProject("core-sql-storage", inFile = Some("core/sql-storage"))(Seq(
  slick,
  slickCats,
  hikariCp,
  postgresql,
  pgSocketFactory,
  slickPg,
  slickPgSprayJson
)).dependsOn(core)


// ---- Common

lazy val common = createProject("common")()

lazy val commonJson = createProject("common-json", inFile = Some("common/json"))(Seq(
  tapirJsonSpray
))

lazy val commonApi = createProject("common-api", inFile = Some("common/api"))(Seq(
  tapirAkkaHttpServer,
  tapirCore,
  tapirOpenApiDocs,
  tapirOpenApiCirceYaml,
  tapirEnumeratum,
  akkaHttpCors,
  jbcrypt
)).dependsOn(commonJson)
  .dependsOn(core)
  .dependsOn(coreSqlStorage)


// ---- Photography

lazy val photography = createProject("photography")()

lazy val entities = createProject("photography-entities", inFile = Some("photography/entities"))(Seq(akkaStream))

lazy val ports = createProject("photography-ports", inFile = Some("photography/ports"))()
  .dependsOn(entities)

lazy val repositories = createProject("photography-repositories", inFile = Some("photography/repositories"))(Seq(googleCloudStorage))
  .dependsOn(ports)
  .dependsOn(commonJson)
  .dependsOn(coreSqlStorage)

lazy val interactors = createProject("photography-interactors", inFile = Some("photography/interactors"))(Seq(scrimage))
  .dependsOn(ports)
  .dependsOn(core)

lazy val api = createProject("photography-api", inFile = Some("photography/api"))()
  .settings(Compile / mainClass := Some("backend.photography.api.Api"))
  .settings(ApiDocker.dockerSettings: _*)
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .dependsOn(commonApi)
  .dependsOn(interactors)
  .dependsOn(repositories)
