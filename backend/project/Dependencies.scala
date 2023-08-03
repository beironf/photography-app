import sbt._

object Dependencies {
  private lazy val akkaV         = "2.6.19"
  private lazy val akkaHttpCorsV = "1.1.3"
  private lazy val tapirV        = "1.0.0-M7"
  private lazy val scalaLoggingV = "3.9.4"
  private lazy val slf4jV        = "1.8.0-beta4"
  private lazy val configV       = "1.4.2"
  private lazy val scalaTestV    = "3.2.11"
  private lazy val catsV         = "2.6.1"
  private lazy val scrimageV     = "4.0.31"
  private lazy val slickV        = "3.3.3"
  private lazy val slickCatsV    = "0.10.4"
  private lazy val slickPgV      = "0.20.2"
  private lazy val jbcryptV      = "0.4"

  lazy val scalaLogging          = "com.typesafe.scala-logging"  %% "scala-logging"            % scalaLoggingV
  lazy val akkaStream            = "com.typesafe.akka"           %% "akka-stream"              % akkaV
  lazy val akkaHttpCors          = "ch.megard"                   %% "akka-http-cors"           % akkaHttpCorsV
  lazy val catsCore              = "org.typelevel"               %% "cats-core"                % catsV
  lazy val slf4j                 = "org.slf4j"                   %  "slf4j-simple"             % slf4jV
  lazy val scalaConfig           = "com.typesafe"                %  "config"                   % configV
  lazy val tapirCore             = "com.softwaremill.sttp.tapir" %% "tapir-core"               % tapirV
  lazy val tapirAkkaHttpServer   = "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server"   % tapirV
  lazy val tapirJsonSpray        = "com.softwaremill.sttp.tapir" %% "tapir-json-spray"         % tapirV
  lazy val tapirOpenApiDocs      = "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % tapirV
  lazy val tapirOpenApiCirceYaml = "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirV
  lazy val tapirEnumeratum       = "com.softwaremill.sttp.tapir" %% "tapir-enumeratum"         % tapirV
  lazy val scrimage              = "com.sksamuel.scrimage"       %  "scrimage-core"            % scrimageV
  lazy val slick                 = "com.typesafe.slick"          %% "slick"                    % slickV
  lazy val slickHikariCP         = "com.typesafe.slick"          %% "slick-hikaricp"           % slickV
  lazy val slickCats             = "com.rms.miu"                 %% "slick-cats"               % slickCatsV
  lazy val slickPg               = "com.github.tminglei"         %% "slick-pg"                 % slickPgV
  lazy val slickPgSprayJson      = "com.github.tminglei"         %% "slick-pg_spray-json"      % slickPgV
  lazy val scalaTest             = "org.scalatest"               %% "scalatest"                % scalaTestV
  lazy val jbcrypt               = "org.mindrot"                 %  "jbcrypt"                  % jbcryptV
}