package uz.scala.routes

import cats.Monad
import cats.effect.Concurrent
import cats.implicits.toFlatMapOps
import org.http4s._
import org.http4s.circe.JsonDecoder
import org.http4s.multipart.Multipart

import uz.scala.algebras.AssetsAlgebra
import uz.scala.domain.FileMeta
import uz.scala.effects.GenUUID
import uz.scala.http4s.syntax.all.deriveEntityEncoder
import uz.scala.http4s.syntax.all.http4SyntaxPartOps
import uz.scala.http4s.utils.Routes
final class RootRoutes[F[_]: JsonDecoder: Monad: Concurrent: GenUUID](assets: AssetsAlgebra[F])
    extends Routes[F] {
  override val path: String = "/assets"
  private val AllowedMediaTypes: List[MediaType] = List(
    MediaType.image.png,
    MediaType.image.jpeg,
  )
  override val public: HttpRoutes[F] = HttpRoutes.of {
    case req @ POST -> Root =>
      req.decode[Multipart[F]] { multipart =>
        val fileParts = multipart.parts.fileParts(AllowedMediaTypes: _*)
        val fileMeta = fileParts.headOption.map { fp =>
          FileMeta(
            fp.body,
            fp.contentType.map(_.mediaType).map(m => s"${m.mainType}/${m.subType}"),
            fp.filename.getOrElse(""),
            fp.contentLength.getOrElse(0L),
          )
        }
        fileMeta
          .fold(BadRequest("File part not found"))(assets.create(_).flatMap(Created(_)))
      }
  }
}
