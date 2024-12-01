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

lazy val insertDynamoDBJar = (project in file("functions/insert-dynamodb-jar"))
  .settings(name := "insert-dynamodb-jar")
  .settings(libraryDependencies ++= Seq(
    "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.12.779",
    "org.typelevel" %% "feral-lambda" % "0.3.1"
  ))
  .settings(
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", "versions", "9", "module-info.class") => MergeStrategy.first
      case x =>
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(x)
    }
  )

lazy val root = (project in file("."))
  .settings(name := "smithy4s-sandbox")
  .aggregate(insertDynamoDB, insertDynamoDBJar)
