ThisBuild / scalaVersion := "2.13.4"
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "sortable-records",
    libraryDependencies += "com.chuusai" %% "shapeless" % "2.4.0-M1"
  )
