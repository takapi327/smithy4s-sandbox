import cats.effect.*
import fs2.io.compression.*
import feral.lambda.*
import org.http4s.ember.client.EmberClientBuilder
import smithy4s.aws.*
import com.amazonaws.dynamodb.*

object Handler extends IOLambda.Simple[Unit, Unit]:

  override type Init = DynamoDB[IO]

  override def init: Resource[IO, Init] =
    for
      httpClient <- EmberClientBuilder.default[IO].build
      awsEnv <- AwsEnvironment.default(httpClient, AwsRegion.AP_NORTHEAST_1)
      dynamodb <- AwsClient(DynamoDB.service, awsEnv)
    yield dynamodb

  override def apply(event: Unit, context: Context[IO], init: Init): IO[Option[Unit]] =
    init.putItem(
      TableName("Smithy4sSandboxTable"),
      Map(
        AttributeName("id") -> AttributeValue.n(NumberAttributeValue("1")),
        AttributeName("name") -> AttributeValue.s(StringAttributeValue("Alice"))
      )
    ).as(None)
