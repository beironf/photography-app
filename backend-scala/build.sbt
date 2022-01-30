import sbt.librarymanagement.ConflictWarning

enablePlugins(JavaAppPackaging)

ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "3.1.0"

conflictWarning := ConflictWarning.disable


// ---------- Project Creator --------

def createProject(nameId: String, inFile: Option[String] = None,
                  dependencies: Seq[ModuleID] = Seq.empty,
                  scala2Dependencies: Seq[ModuleID] = Seq.empty): Project =
  Project.apply(nameId, file(inFile.getOrElse(nameId)))
    .settings(
      name := nameId,
      commonSettings,
      libraryDependencies ++= dependencies ++ scala2Dependencies.map(_.cross(CrossVersion.for3Use2_13))
    )


// ---------- Domains ----------------

lazy val backend = createProject("backend", inFile = Some("."))
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    photoApi
  )

lazy val core = createProject("core",
  dependencies = Seq(
    dependencies.scalaLogging
  ),
  scala2Dependencies = Seq(
    scala2Dependencies.akkaActor,
    scala2Dependencies.akkaStream
  ))

lazy val common = createProject("common")

lazy val commonJson = createProject("common-json", inFile = Some("common/json"),
  dependencies = Seq(
    dependencies.scalaz
  ),
  scala2Dependencies = Seq(
    scala2Dependencies.sprayJson
  ))

lazy val commonApi = createProject("common-api", inFile = Some("common/api"),
  scala2Dependencies = Seq(
    scala2Dependencies.akkaHttp,
    scala2Dependencies.akkaHttpJson,
  ))
  .dependsOn(core)
  .dependsOn(commonJson)

lazy val photo = createProject("photo")

lazy val photoApi = createProject("photo-api", inFile = Some("photo/api"))
  .dependsOn(commonApi)

lazy val photoEntities = createProject("photo-entities", inFile = Some("photo/entities"))


// ---------- Dependencies -----------

lazy val dependencies = new {
  private val scalaTestV       = "3.2.10"
  private val scalaLoggingV    = "3.9.4"
  private val scalazV          = "7.4.0-M10"

  val scalaz       = "org.scalaz"                 %% "scalaz-core"   % scalazV
  val scalaTest    = "org.scalatest"              %% "scalatest"     % scalaTestV
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV
}

lazy val scala2Dependencies = new {
  private val akkaHttpV        = "10.2.7"
  private val akkaV            = "2.6.18"
  private val sprayJsonV       = "1.3.6"

  // Not for Scala 3 - Use .map(_.cross(CrossVersion.for3Use2_13)) on these
  val sprayJson       = "io.spray"          %% "spray-json"           % sprayJsonV
  val akkaHttp        = "com.typesafe.akka" %% "akka-http"            % akkaHttpV
  val akkaHttpJson    = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV
  val akkaActor       = "com.typesafe.akka" %% "akka-actor"           % akkaV
  val akkaStream      = "com.typesafe.akka" %% "akka-stream"          % akkaV
  val akkaTestkit     = "com.typesafe.akka" %% "akka-testkit"         % akkaV
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
