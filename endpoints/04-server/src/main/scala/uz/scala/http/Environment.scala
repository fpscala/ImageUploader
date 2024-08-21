package uz.scala.http

import cats.effect.Async

import uz.scala.Algebras
import uz.scala.http4s.HttpServerConfig
case class Environment[F[_]: Async](
    config: HttpServerConfig,
    algebras: Algebras[F],
  )
