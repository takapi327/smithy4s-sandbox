import cats.syntax.all.*

import cats.effect.*

import fs2.compression.Compression

import org.http4s.client.Client

import smithy4s.*
import smithy4s.aws.*

object LocalstackClient:

  private def env[F[_]: Async: Compression](
                                             client: Client[F],
                                             region: AwsRegion
                                           ): AwsEnvironment[F] =
    AwsEnvironment.make[F](
      client,
      Async[F].pure(region),
      Async[F].pure(AwsCredentials.Default("dummy", "dummy", None)),
      Async[F].realTime.map(_.toSeconds).map(Timestamp(_, 0))
    )

  def apply[F[_]: Async: Compression, Alg[_[_, _, _, _, _]]](
                                                              client:  Client[F],
                                                              region:  AwsRegion,
                                                              service: Service[Alg]
                                                            ): Resource[F, service.Impl[F]] =
    AwsClient(service, env[F](LocalstackProxy[F](client), region))
