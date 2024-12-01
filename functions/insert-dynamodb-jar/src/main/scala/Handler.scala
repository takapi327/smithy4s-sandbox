import scala. jdk. CollectionConverters. *

import com.amazonaws.services.dynamodbv2.*
import com.amazonaws.services.dynamodbv2.model.AttributeValue

import cats.effect.*

import feral.lambda.*

object Handler extends IOLambda.Simple[Unit, String]:
  
  private val dynamodb = AmazonDynamoDBClientBuilder.defaultClient()

  override def apply(event: Unit, context: Context[IO], init: Init): IO[Option[String]] =
    IO.blocking(dynamodb.putItem(
      "Smithy4sSandboxTable",
      Map(
        "id" -> new AttributeValue("2"),
        "name" -> new AttributeValue("Bob")
      ).asJava
    )).as(Some("Success"))
