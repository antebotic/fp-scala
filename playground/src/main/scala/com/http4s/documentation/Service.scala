package com.http4s.documentation

import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.implicits._
import org.http4s.dsl.io._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.Router
import org.http4s.circe._
import scala.concurrent.ExecutionContext.Implicits.global
import io.circe.generic.auto._
import io.circe.syntax._


object Service extends IOApp {
  val helloWorldService = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name => {
      Ok(s"Hello $name")
    }
  }

  case class Tweet(id: Int, message: String)

  def getTweet(tweetId: Int): IO[Tweet] = tweetId match {
    case 1 => Tweet(1, "Hello I am Hogar the terrible").pure[IO]
    case 2 => Tweet(2, "This is the second one").pure[IO]
    case _ => Tweet(3, "I should learn doobie").pure[IO]
  }

  def getPopularTweets(): IO[Seq[Tweet]] = Seq(1,2,3).map(getTweet).sequence

  val tweetService = HttpRoutes.of[IO] {
    case GET -> Root / "tweets" / "popular" =>
      getPopularTweets.flatMap { tweets => Ok(tweets.asJson) }

    case GET -> Root / "tweets" / IntVar(tweetId) =>
      getTweet(tweetId).flatMap { tweet => Ok(tweet.asJson) }
  }

  val services = helloWorldService.combineK(tweetService)
  //also possible:  val services = helloWorldService <+> tweetService

  val httpApp = Router("/" -> helloWorldService, "/api" -> services).orNotFound

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(5544, "localhost")
      .withHttpApp(httpApp)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
