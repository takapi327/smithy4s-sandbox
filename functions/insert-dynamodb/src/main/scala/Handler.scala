import cats.effect.*

import fs2.io.compression.*

import feral.lambda.*

import org.http4s.ember.client.EmberClientBuilder

import smithy4s.aws.*
import com.amazonaws.dynamodb.*

object Handler extends IOLambda.Simple[Unit, String]:

  override type Init = DynamoDB[IO]

  override def init: Resource[IO, Init] =
    for
      httpClient <- EmberClientBuilder.default[IO].build
      dynamodb <- LocalstackClient(httpClient, AwsRegion.AP_NORTHEAST_1, DynamoDB.service)
    yield dynamodb

  override def apply(event: Unit, context: Context[IO], init: Init): IO[Option[String]] =
    init.putItem(
      TableName("Smithy4sSandboxTable"),
      Map(
        AttributeName("id") -> AttributeValue.s(StringAttributeValue("1")),
        AttributeName("name") -> AttributeValue.s(StringAttributeValue("Alice"))
      )
    ).as(Some("Success"))
