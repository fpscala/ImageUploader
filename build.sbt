import Dependencies.*

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "ImageUploader"
  )

lazy val common =
  project
    .in(file("common"))
    .settings(
      name := "common",
      libraryDependencies ++=
        Dependencies.io.circe.all ++
          eu.timepit.refined.all ++
          com.github.pureconfig.all ++
          com.beachape.enumeratum.all ++
          Seq(
            uz.scala.common,
            org.typelevel.cats.core,
            org.typelevel.cats.effect,
            org.typelevel.log4cats,
            ch.qos.logback,
          ),
    )
    .dependsOn(LocalProject("support_logback"))

lazy val integrations = project
  .in(file("integrations"))
  .settings(
    name := "integrations"
  )

lazy val supports = project
  .in(file("supports"))
  .settings(
    name := "supports"
  )

lazy val endpoints = project
  .in(file("endpoints"))
  .settings(
    name := "endpoints"
  )

addCommandAlias(
  "styleCheck",
  "all scalafmtSbtCheck; scalafmtCheckAll; Test / compile; scalafixAll --check",
)

Global / lintUnusedKeysOnLoad := false
Global / onChangedBuildSource := ReloadOnSourceChanges
