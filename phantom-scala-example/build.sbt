name := "phantom-scala-example"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  // Database dependencies
  "com.outworkers" % "phantom-dsl_2.12" % "2.30.0",
  // scala logging
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "org.slf4j" % "slf4j-simple" % "1.7.25"
)
