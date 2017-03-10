
name := "CountMeUp"

version := "1.0"

scalaVersion := "2.12.1"

lazy val deps = Seq (
  "org.scalatest" % "scalatest_2.12" % "3.2.0-SNAP4",
  "org.mockito" % "mockito-all" % "1.10.19"
)

libraryDependencies ++= deps