package uz.scala

import cats.MonadThrow
import org.typelevel.log4cats.Logger

import uz.scala.algebras._
import uz.scala.aws.s3.S3Client
import uz.scala.effects.Calendar
import uz.scala.effects.GenUUID

case class Algebras[F[_]](
    assets: AssetsAlgebra[F]
  )

object Algebras {
  def make[F[_]: MonadThrow: Calendar: GenUUID: Logger: Lambda[M[_] => fs2.Compiler[M, M]]](
      s3Client: S3Client[F]
    ): Algebras[F] =
    Algebras[F](
      assets = AssetsAlgebra.make[F](s3Client)
    )
}
