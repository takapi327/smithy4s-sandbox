
import org.typelevel.ci.*

import fs2.compression.Compression

import cats.effect.*

import org.http4s.*
import org.http4s.client.Client

object LocalstackProxy:
  def apply[F[_]: Async: Compression](client: Client[F]): Client[F] = Client { req =>
    val request = req
      .withUri(
        req.uri.copy(
          scheme = Some(Uri.Scheme.http),
          authority = req.uri.authority.map(
            _.copy(
              host = Uri.RegName("host.docker.internal"),
              port = Some(4566)
            )
          )
        )
      )
      .putHeaders(Header.Raw(ci"host", "host.docker.internal"))

    client.run(request)
  }
