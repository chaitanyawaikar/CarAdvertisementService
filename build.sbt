name := """advertisement-application"""

version := "2.6.x"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.7"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies ++= Seq(
  guice,
  "com.typesafe.play" %% "play-slick" % "3.0.3",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3",
  "org.mockito" % "mockito-all" % "1.10.19" % Test,
  "com.h2database" % "h2" % "1.4.197",
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "net.codingwell" %% "scala-guice" % "4.1.0",
  "com.typesafe.slick" %% "slick-codegen" % "3.2.1",
  "com.typesafe.slick" %% "slick-testkit" % "3.2.3" % Test,
  specs2 % Test
)
