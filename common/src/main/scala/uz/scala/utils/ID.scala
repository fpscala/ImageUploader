package uz.scala.utils

import cats.Functor
import cats.implicits.toFunctorOps

import uz.scala.effects.GenUUID
import uz.scala.effects.IsUUID

object ID {
  def make[F[_]: Functor: GenUUID, A: IsUUID]: F[A] =
    GenUUID[F].make.map(IsUUID[A].uuid.get)

  def read[F[_]: Functor: GenUUID, A: IsUUID](str: String): F[A] =
    GenUUID[F].read(str).map(IsUUID[A].uuid.get)
}
