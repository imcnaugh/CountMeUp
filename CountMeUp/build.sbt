
name := "CountMeUp"

version := "1.0"

scalaVersion := "2.12.1"

lazy val deps = Seq (
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.mockito" % "mockito-all" % "1.10.19",
  "org.mongodb.scala" %% "mongo-scala-driver" % "1.2.1"
)

libraryDependencies ++= deps