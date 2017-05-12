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
  evolutions,
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "bootstrap" % "3.1.1-2",
  "org.jsoup" % "jsoup" % "1.10.2"
)


fork in run := false