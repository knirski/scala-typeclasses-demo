lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "pl.knirski",
      scalaVersion := "2.12.7"
    )),
    name := "scala-typeclasses-demo"
  )

libraryDependencies += "io.circe" %% "circe-core" % "0.10.0"
libraryDependencies += "io.circe" %% "circe-generic" % "0.10.0"
