package uz.scala.setup

import cats.effect.Async
import cats.effect.Resource
import cats.effect.std.Console
import cats.effect.std.Dispatcher
import eu.timepit.refined.pureconfig._
import org.typelevel.log4cats.Logger
import pureconfig.generic.auto.exportReader

import uz.scala.Algebras
import uz.scala.aws.s3.S3Client
import uz.scala.http.{ Environment => ServerEnvironment }
import uz.scala.utils.ConfigLoader

case class Environment[F[_]: Async: Logger](
    config: Config,
    s3Client: S3Client[F],
  ) {
  private val algebras: Algebras[F] = Algebras.make[F](s3Client)
  lazy val toServer: ServerEnvironment[F] =
    ServerEnvironment(
      config = config.http,
      algebras = algebras,
    )
}
object Environment {

  def make[F[_]: Async: Console: Logger]: Resource[F, Environment[F]] =
    for {
      config <- Resource.eval(ConfigLoader.load[F, Config])
      s3Client <- S3Client.resource(config.awsConfig)
    } yield Environment[F](config, s3Client)
}
