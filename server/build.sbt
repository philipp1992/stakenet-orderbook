import scalapb.compiler.Version.{grpcJavaVersion, scalapbVersion}
name := "orderbook-server"
organization := "io.stakenet"
scalaVersion := "2.13.3"

fork in Test := true

scalacOptions ++= Seq(
  "-Werror",
  "-unchecked",
  "-deprecation",
  "-feature",
  "-target:jvm-1.8",
  "-encoding",
  "UTF-8",
  "-Xsource:3",
  "-Wconf:src=src_managed/.*:silent",
  "-Xlint:missing-interpolator",
  "-Xlint:adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Ywarn-unused"
)

scalacOptions in (Compile, doc) ++= Seq(
  "-no-link-warnings"
)

// play
lazy val root = (project in file("."))
  .enablePlugins(PlayScala, JavaAgent)
  .disablePlugins(PlayLayoutPlugin)

// Some options are very noisy when using the console and prevent us using it smoothly, let's disable them
val consoleDisabledOptions = Seq("-Xfatal-warnings", "-Ywarn-unused", "-Ywarn-unused-import")
scalacOptions in (Compile, console) ~= (_ filterNot consoleDisabledOptions.contains)

// initialization script for the console
val consoleInitialCommandsFile = "console.scala"
excludeFilter := HiddenFileFilter || consoleInitialCommandsFile
lazy val consoleScript = scala.util.Try(scala.io.Source.fromFile(consoleInitialCommandsFile).mkString).getOrElse("")
initialCommands in console := consoleScript

// docs are huge and unnecessary
sources in (Compile, doc) := Nil

// play specific dependencies
libraryDependencies ++= Seq(guice, evolutions, jdbc, ws)

// scalapb
PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

libraryDependencies ++= Seq(
  "io.grpc" % "grpc-netty-shaded" % grpcJavaVersion,
  "io.netty" % "netty-tcnative-boringssl-static" % "2.0.25.Final",
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf",
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion
)

//
import play.sbt.routes.RoutesKeys
RoutesKeys.routesImport := Seq.empty

//
libraryDependencies += "com.google.inject" % "guice" % "5.0.1"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.32"
libraryDependencies += "ch.qos.logback" % "logback-core" % "1.2.3"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
libraryDependencies += "org.mockito" %% "mockito-scala" % "1.16.49" % Test

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.6.10" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-stream-typed" % "2.6.10"

libraryDependencies ++= Seq(
  "com.beachape" %% "enumeratum" % "1.7.0"
)

libraryDependencies += "com.github.andyglow" %% "websocket-scala-client" % "0.4.0" % Compile exclude ("org.slf4j", "slf4j-simple")
libraryDependencies += "org.apache.commons" % "commons-compress" % "1.21"

libraryDependencies += "org.playframework.anorm" %% "anorm" % "2.6.10"
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.24"

libraryDependencies += "io.scalaland" %% "chimney" % "0.6.1"

libraryDependencies += "io.sentry" % "sentry-logback" % "4.3.0"

libraryDependencies += "io.kamon" %% "kamon-bundle" % "2.1.21"
libraryDependencies += "io.kamon" %% "kamon-apm-reporter" % "2.1.21"

libraryDependencies ++= Seq(
  "com.spotify" % "docker-client" % "8.16.0" % "test",
  "com.whisk" %% "docker-testkit-scalatest" % "0.11.0" % "test",
  "com.whisk" %% "docker-testkit-impl-spotify" % "0.9.9" % "test"
)

resolvers += Resolver.JCenterRepository
libraryDependencies += "net.katsstuff" %% "ackcord" % "0.17.1"

// required because AckCord is overriding the akka version to 2.6.6, see https://github.com/akka/akka/issues/29351
libraryDependencies += "com.typesafe.akka" %% "akka-serialization-jackson" % "2.6.6"

libraryDependencies += "org.web3j" % "core" % "5.0.0"
