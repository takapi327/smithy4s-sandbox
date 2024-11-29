ThisBuild / organization := "io.github.takapi327"
ThisBuild / scalaVersion := "3.5.2"

lazy val insertDynamoDB = (project in file("functions/insert-dynamodb"))
  .settings(name := "insert-dynamodb")
  .settings(libraryDependencies ++= Seq(
    "com.disneystreaming.smithy4s" %%% "smithy4s-aws-http4s" % smithy4sVersion.value,
    "org.http4s" %%% "http4s-ember-client" % "0.23.29"
  ))
  .settings(smithy4sAwsSpecs ++= Seq(AWS.dynamodb))
  .enablePlugins(LambdaJSPlugin, Smithy4sCodegenPlugin)

lazy val root = (project in file("."))
  .settings(name := "smithy4s-sandbox")
  .aggregate(insertDynamoDB)
