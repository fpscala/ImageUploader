package uz.scala.http4s

import scala.util.control.NonFatal

import cats.MonadThrow
import cats.implicits.catsSyntaxFlatMapOps
import io.circe.Json
import org.http4s.HttpRoutes
import org.http4s.MalformedMessageBodyFailure
import org.http4s.Response
import org.http4s.dsl.Http4sDsl
import org.typelevel.log4cats.Logger

import uz.scala.Mode
import uz.scala.Mode.Production
import uz.scala.MultipartDecodeError
import uz.scala.exception.AError
import uz.scala.http4s.syntax.all.deriveEntityEncoder
import uz.scala.http4s.utils.HttpErrorHandler
import uz.scala.syntax.all.genericSyntaxGenericTypeOps
class LiveHttpErrorHandler[F[_]: MonadThrow](
    implicit
    logger: Logger[F]
  ) extends HttpErrorHandler[F, AError]
       with Http4sDsl[F] {
  private val handler: PartialFunction[Throwable, F[Response[F]]] = {
    case error: AError =>
      logger.info(error)("Error occurred") >>
        Ok(errorBody(error))
    case error: MalformedMessageBodyFailure =>
      logger.info(error)("Invalid json entered") >>
        UnprocessableEntity(error.details.toJson)
    case error: MultipartDecodeError =>
      logger.info(error)("Invalid form data entered") >>
        UnprocessableEntity(error.cause.toJson)
    case error: IllegalArgumentException =>
      logger.info(error)("Incorrect argument entered") >>
        UnprocessableEntity(error.getMessage.replace("requirement failed: ", "").toJson)
    case NonFatal(throwable) =>
      logger.error(throwable)("Error occurred while processing request") >> {
        if (Mode.current == Production)
          InternalServerError("Internal server error")
        else InternalServerError(errorBody(throwable))
      }

    case throwable =>
      logger.error(throwable)("Error occurred while processing request") >>
        BadRequest("Something went wrong. Please try again in a few minutes".toJson)
  }

  private def errorBody: PartialFunction[Throwable, Json] = {
    case error =>
      Json.obj("errors" -> Json.arr(Json.obj("message" -> Json.fromString(error.getMessage))))
  }

  override def handle(routes: HttpRoutes[F]): HttpRoutes[F] =
    RoutesHttpErrorHandler(routes)(handler)
}
