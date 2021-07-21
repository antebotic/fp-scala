package com.http4s.documentation

import cats._
import cats.implicits._
import cats.effect._
import io.circe.syntax._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder


object DSL extends IOApp {

  val service: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case _ => IO(Response(Status.Ok))
  }

  def service[F[_]: Monad]: HttpRoutes[F] = {
    val service = HttpRoutes.of[F] {
      case _ => Response[F](Status.Ok).pure[F]
    }

    service
  }

  def routes[F[_]: Monad]: HttpApp[F] =
    service[F].orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](runtime.compute)
      .bindHttp(9876, "localhost")
      .withHttpApp(routes)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
