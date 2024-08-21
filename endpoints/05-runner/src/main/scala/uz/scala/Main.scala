package uz.scala

import cats.effect.Async
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.effect.std.Console
import cats.implicits.toTraverseOps
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import uz.scala.setup.Environment

object Main extends IOApp {
  implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]
  private def runnable[F[_]: Async: Logger: Console]: Resource[F, List[F[ExitCode]]] =
    for {
      env <- Environment.make[F]
      httpModule <- HttpModule.make[F](env.toServer)
    } yield List(httpModule)

  override def run(
      args: List[String]
    ): IO[ExitCode] =
    runnable[IO]
      .use { runners =>
        for {
          fibers <- runners.traverse(_.start)
          _ <- fibers.traverse(_.join)
          _ <- IO.never[Unit]
        } yield ExitCode.Success
      }
}
