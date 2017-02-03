name := "rxScalaExamples"

version := "1.0"

scalaVersion := "2.12.1"

val sl4jVersion = "1.7.21"

mainClass in (Compile, run) := Some("com.lkuligin.rxScalaExamples.Application")

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "org.slf4j" % "slf4j-api" % sl4jVersion,
  "io.reactivex" % "rxscala_2.11" % "0.26.2",
  "com.google.inject" % "guice" % "4.0",
  "com.typesafe" % "config" % "1.3.0",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "net.codingwell" %% "scala-guice" % "4.1.0",
  "org.eclipse.jetty" % "example-jetty-embedded" % "9.3.6.v20151106" exclude("org.eclipse.jetty.tests", "test-mock-resources")
)
