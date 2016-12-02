name := "aero"

lazy val commonSettings = Seq(
  organization := "org.rntech",
  version := "0.1.0",
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )
)

lazy val worker = project.in(file("aero-worker"))
	.settings(commonSettings: _*)

lazy val master = project.in(file("aero-master"))
	.settings(commonSettings: _*)

lazy val flow = project.in(file("aero-flow"))
	.settings(commonSettings: _*)

