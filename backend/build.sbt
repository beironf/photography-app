import Dependencies._
import BuildSettings._

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / organization := "com.beironf"
ThisBuild / version := s"$gitHash"

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

lazy val commonModel = createProject("common-model", inFile = Some("common/model"))()

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
)).dependsOn(commonModel)
  .dependsOn(commonJson)
  .dependsOn(core)
  .dependsOn(coreSqlStorage)


// ---- Photo

lazy val photo = createProject("photo")()

lazy val photoEntities = createProject("photo-entities", inFile = Some("photo/entities"))()

lazy val photoPorts = createProject("photo-ports", inFile = Some("photo/ports"))()
  .dependsOn(photoEntities)

lazy val photoAdapters = createProject("photo-adapters", inFile = Some("photo/adapters"))()
  .dependsOn(photoPorts)
  .dependsOn(commonJson)
  .dependsOn(coreSqlStorage)

lazy val photoInteractors = createProject("photo-interactors", inFile = Some("photo/interactors"))()
  .dependsOn(photoPorts)


// ---- Exif (Image metadata)

lazy val exif = createProject("exif")()

lazy val exifEntities = createProject("exif-entities", inFile = Some("exif/entities"))()

lazy val exifPorts = createProject("exif-ports", inFile = Some("exif/ports"))()
  .dependsOn(exifEntities)

lazy val exifAdapters = createProject("exif-adapters", inFile = Some("exif/adapters"))()
  .dependsOn(exifPorts)
  .dependsOn(coreSqlStorage)

lazy val exifInteractors = createProject("exif-interactors", inFile = Some("exif/interactors"))()
  .dependsOn(exifPorts)


// ---- Image

lazy val image = createProject("image")()

lazy val imageEntities = createProject("image-entities", inFile = Some("image/entities"))(Seq(
  akkaStream
))

lazy val imagePorts = createProject("image-ports", inFile = Some("image/ports"))()
  .dependsOn(imageEntities)

lazy val imageAdapters = createProject("image-adapters", inFile = Some("image/adapters"))(Seq(
  googleCloudStorage
)).dependsOn(imagePorts)
  .dependsOn(coreSqlStorage)

lazy val imageInteractors = createProject("image-interactors", inFile = Some("image/interactors"))()
  .dependsOn(imagePorts)

// ---- API

lazy val api = createProject("api", inFile = Some("api"))(Seq(scrimage))
  .settings(Compile / mainClass := Some("backend.api.Api"))
  .settings(ApiDocker.dockerSettings: _*)
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .dependsOn(commonApi)
  .dependsOn(photoInteractors)
  .dependsOn(photoAdapters)
  .dependsOn(imageInteractors)
  .dependsOn(imageAdapters)
  .dependsOn(exifInteractors)
  .dependsOn(exifAdapters)
