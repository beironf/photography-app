import sbt.librarymanagement.ConflictWarning

enablePlugins(JavaAppPackaging)

name := "backend"
version := "1.0"
scalaVersion := "3.1.0"

conflictWarning := ConflictWarning.disable

// ---------- Domains ----------------

lazy val backend = project
  .in(file("."))
  .settings(settings)
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    core,
    common,
    photo,
    photoApi
  )

lazy val core = project
  .settings(
    name := "core",
    settings,
    libraryDependencies ++= commonDependencies
  )

lazy val common = project
  .settings(
    name := "common",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(core)

lazy val photo = project
  .settings(
    name := "photo",
    settings
  )
  .dependsOn(common, core)

lazy val photoApi = project
  .in(file("photo/api"))
  .settings(
    name := "photo-api",
    settings
  )
  .dependsOn(common, core)

// ---------- Dependencies -----------

lazy val dependencies = new {
  private val akkaHttpV        = "10.2.7"
  private val akkaV            = "2.6.18"
  private val scalaTestV       = "3.2.10"
  private val circeV           = "0.14.1"
  private val scalaLoggingV    = "3.9.4"
  private val akkaHttpCirceV   = "1.39.2"

  val circe        = "io.circe"                   %% "circe-core"    % circeV
  val circeParser  = "io.circe"                   %% "circe-parser"  % circeV
  val circeGeneric = "io.circe"                   %% "circe-generic" % circeV
  val scalaTest    = "org.scalatest"              %% "scalatest"     % scalaTestV
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV

  // Not for Scala 3 .. use .map(_.cross(CrossVersion.for3Use2_13))
  val akkaHttp        = "com.typesafe.akka" %% "akka-http"         % akkaHttpV
  val akkaActor       = "com.typesafe.akka" %% "akka-actor"        % akkaV
  val akkaStream      = "com.typesafe.akka" %% "akka-stream"       % akkaV
  val akkaTestkit     = "com.typesafe.akka" %% "akka-testkit"      % akkaV
  val akkaHttpCirce   = "de.heikoseeberger" %% "akka-http-circe"   % akkaHttpCirceV
  val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV // % "test"
}

lazy val commonDependencies = Seq(
  dependencies.scalaTest % "test",
  dependencies.scalaLogging
) ++ Seq(
  dependencies.akkaActor,
  dependencies.akkaHttp,
  dependencies.akkaStream,
  dependencies.akkaTestkit
).map(_.cross(CrossVersion.for3Use2_13))

// ---------- Settings ---------------

lazy val settings = commonSettings

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
