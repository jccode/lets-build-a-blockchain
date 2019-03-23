name := "lets-build-a-blockchain"

version := "1.0"

scalaVersion in ThisBuild := "2.12.3"

resolvers in ThisBuild += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers in ThisBuild += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

lazy val playSettings = Seq(
)

// libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )

lazy val stage1 = (project in file("stage1"))
  .enablePlugins(PlayScala)
  .settings(
    libraryDependencies ++= Seq( guice, ws )
  )

lazy val stage2 = (project in file("stage2"))
  .enablePlugins(PlayScala)
  .settings(
    libraryDependencies ++= Seq( guice, ws )
  )

lazy val stage3 = (project in file("stage3"))

lazy val stage4 = (project in file("stage4"))

lazy val stage5 = (project in file("stage5"))

lazy val stage6 = (project in file("stage6"))

lazy val root = (project in file("."))
  .aggregate(stage1, stage2, stage3, stage4, stage5, stage6)
