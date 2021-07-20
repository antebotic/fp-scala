package com.rockthejvm

import java.time.Year
import java.util.UUID

import cats._
import cats.effect._
import cats.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.dsl._
import org.http4s.dsl.impl._
import org.http4s.headers
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.circe._
import org.http4s.server.blaze.BlazeServerBuilder

import scala.collection.mutable
import scala.util.Try

object Http4sTutorial extends IOApp{
  //movie database
  type Actor = String
  final case class Movie(id: String, title: String, year: Int, actors: List[Actor], director: String)
  final case class Director(firstName: String, lastName: String){
    override def toString: Actor = s"$firstName $lastName"
  }
  final case class DirectorDetails(firstName: String, lastName: String, genre: String)

  //internal "database"
  val snjl: Movie = Movie(
    "6bcbca1e-efd3-411d-9f7c-14b872444fce",
    "Zack Snyder's Justice League",
    2021,
    List("Henry Cavill", "Gal Godot", "Ezra Miller", "Ben Affleck", "Ray Fisher", "Jason Momoa"),
    "Zack Snyder"
  )

  val movies : Map[String, Movie] = Map(snjl.id -> snjl)

  // "business logic :)"
  private def findMovieById(movieId: UUID): Option[Movie] =
    movies.get(movieId.toString)

  private def findMoviesByDirector(director: String): List[Movie] =
    movies.values.filter(_.director == director).toList


  /*
    - GET all movies for a director under a given year
    - GET all actors for a movie
    - GET details about director
    - POST add a new director
  */

  // Request -> F[Option[Response]]
  // HttpRouts[F] is an alias for the above function
  implicit val yearQueryParamDecoder: QueryParamDecoder[Year] =
    QueryParamDecoder[Int].emap { yearInt =>
      Try(Year.of(yearInt))
        .toEither
        .leftMap{ e =>
          ParseFailure(e.getMessage, e.getMessage) // second param provides details
        }
    }

  object DirectorQueryParamMatcher extends QueryParamDecoderMatcher[String]("director")
  object YearQueryParamMatcher extends OptionalValidatingQueryParamDecoderMatcher[Year]("year")
  // GET /movies?director=Zack+Ponytail&year=2021

  def movieRoutes[F[_]: Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "movies" :? DirectorQueryParamMatcher(director) +& YearQueryParamMatcher(maybeYear) =>
        val moviesByDirector = findMoviesByDirector(director)
        maybeYear match {
          case Some(validatedYear) =>
            validatedYear.fold(
              _     => BadRequest("Year is badly formatted"),
              year  => Ok(
                moviesByDirector
                  .filter(_.year == year.getValue)
                  .asJson
              )
          )
          case None => Ok(moviesByDirector.asJson)
        }
      case GET -> Root / "movies" / UUIDVar(movieId) / "actors" =>
        findMovieById(movieId).map(_.actors) match {
          case Some(act) => Ok(act.asJson)
          case None      => NotFound(s"No movie with id $movieId found in the database")
        }
    }
  }

  object DirectorPath {
    def unapply(str: String): Option[Director] = {
      Try {
        val tokens = str.split(" ")
        Director(tokens(0), tokens(1))
      }.toOption
    }
  }

  //in memory mock database for simplicity
  val directorDetailsDB: mutable.Map[Director, DirectorDetails] =
    mutable.Map(Director("Clint", "Eastwood") -> DirectorDetails("Clint", "Eastwood", "Dirty harry"))

  def directorRoutes[F[_]: Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F]{
      case GET -> Root / "directors" / DirectorPath(director) =>
        directorDetailsDB.get(director) match {
          case Some(d)  => Ok(d.asJson)
          case None     => NotFound(s"No director under $director found")
        }
    }
  }

  //Composing routes
  def allRoutes[F[_]: Monad]: HttpRoutes[F] =
    movieRoutes[F] <+> directorRoutes[F] // cats.syntax.semigroupK._ is the origin of <+>


  def allRoutesComplete[F[_]: Monad]: HttpApp[F] =
    allRoutes[F].orNotFound

  override def run(args: List[String]): IO[ExitCode] = {
    val apis = Router(
      "/api" -> movieRoutes[IO],
      "/api/admin" -> directorRoutes[IO]
    ).orNotFound

    BlazeServerBuilder[IO](runtime.compute)
      .bindHttp(4428, "localhost")
      .withHttpApp(allRoutesComplete)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
