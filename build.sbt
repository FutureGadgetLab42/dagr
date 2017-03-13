name := """dagr"""

version := "1.0"

lazy val dagr = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  "mysql" % "mysql-connector-java" % "5.1.18",
  cache,
  javaWs,
  evolutions
)
