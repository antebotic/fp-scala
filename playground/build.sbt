scalaVersion := "2.13.1"
ThisBuild / organization := "com.example"

lazy val hello = (project in file("."))
  .settings(
    name := "http4s example"
  )

val CatsVersion   = "2.1.1"
val Http4sVersion = "1.0.0-M21"
val CirceVersion  = "0.14.0-M5"
val ScalaTestVersion = "3.2.2"
val DisciplineVersion = "1.0.0"
val DisciplineScalaTestVersion = "2.1.1"

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.3" cross CrossVersion.full)

libraryDependencies += "org.typelevel" 	%% "cats-core"            % CatsVersion
libraryDependencies += "org.typelevel" 	%% "cats-laws"            % CatsVersion
libraryDependencies += "org.typelevel" 	%% "discipline-core"      % DisciplineVersion
libraryDependencies += "org.typelevel" 	%% "discipline-scalatest" % DisciplineScalaTestVersion
libraryDependencies += "org.scalatest" 	%% "scalatest"            % ScalaTestVersion
libraryDependencies += "org.http4s"   	%% "http4s-blaze-server"  % Http4sVersion
libraryDependencies += "org.http4s"	    %% "http4s-circe" 	      % Http4sVersion
libraryDependencies += "org.http4s"	    %% "http4s-dsl" 	        % Http4sVersion
libraryDependencies += "io.circe"	      %% "circe-generic"	      % CirceVersion
