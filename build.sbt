import sbt._
import Keys._

name := "aero"

lazy val commonSettings = Seq(
  organization := "org.rntech",
  version := "0.1.0",
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http" % "10.0.0",
    "org.scala-lang.modules" %% "scala-pickling" % "0.10.1",   //TODO worker specific move.
    "org.typelevel" %% "cats-core" % "0.8.1",                  //TODO worker specific move.
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )
)

lazy val worker = project.in(file("aero-worker"))
	.settings(commonSettings: _*)

lazy val master = project.in(file("aero-master"))
	.settings(commonSettings: _*)

lazy val flow = project.in(file("aero-flow"))
	.settings(commonSettings: _*)

