import java.io.IOException
import scala.util._

sealed trait HttpMethod

case object GET extends HttpMethod
final case class HttpRequest(method: HttpMethod, url: String)
final case class HttpResponse(status: Int)

def doRequest(req: HttpRequest): HttpResponse =
  if(math.random < 0.5) throw new IOException("boom!")
  else HttpResponse(200)

def executeRequest(req: HttpRequest): Option[HttpResponse] =
  try {
    Some(doRequest(req))
  } catch {
    case _: Exception => None
  }

def executeRequest2(req: HttpRequest): Either[String, HttpResponse] =
  try {
    Right(doRequest(req))
  } catch {
    case _: Exception => Left("Sorry")
  }

def executeRequest3(req: HttpRequest): Try[HttpResponse] =
  try {
    Success(doRequest(req))
  } catch {
    case e: Exception => Failure(e)
  }

executeRequest(HttpRequest(GET, "www.something.com"))
executeRequest2(HttpRequest(GET, "www.s.co"))
executeRequest3(HttpRequest(GET, "www.123.io"))

//MonadError instances
import cats._
import cats.implicits._

val optionME: MonadError[Option, Unit] = new MonadError[Option, Unit] {
  override def raiseError[A](e: Unit): Option[A]= None

  override def handleErrorWith[A](fa: Option[A])(f: Unit => Option[A]): Option[A] = {
    fa match {
      case Some(a) => Some(a)
      case None    => f(())
    }
    // also possible fa.orElse(())
  }


  override def pure[A](x: A): Option[A] = Some(x)

  override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
    fa match {
      case Some(a) => f(a)
      case _    => None
    }

  override def tailRecM[A, B](a: A)(f: A => Option[Either[A, B]]) = ???
}

def eitherME[E]: MonadError[Either[E, *], E] = new MonadError[Either[E, *], E]{
  override def raiseError[A](e: E): Either[E, A] = Left(e)

  override def handleErrorWith[A](fa: Either[E, A])(f: E => Either[E, A]): Either[E, A] =
    fa match {
      case Left(e)  => f(e)
      case Right(v) => Right(v)
    }

  override def pure[A](x: A): Either[E, A] = Right(x)

  override def flatMap[A, B](fa: Either[E, A])(f: A => Either[E, B]): Either[E, B] = fa.flatMap(f)

  override def tailRecM[A, B](a: A)(f: A => Either[E, Either[A, B]]): Either[E, B] = ???
}

val tryME: MonadError[Try, Throwable] = new MonadError[Try, Throwable]{
  override def raiseError[A](e: Throwable): Try[A] = Failure(e)

  override def handleErrorWith[A](fa: Try[A])(f: Throwable => Try[A]): Try[A] =
    fa match {
      case Success(v) => Success(v)
      case Failure(t) => f(t)
    }

  override def pure[A](x: A): Try[A] = Success(x)

  override def flatMap[A, B](fa: Try[A])(f: A => Try[B]): Try[B] =
    fa match {
      case Success(v) => f(v)
      case Failure(t) => Failure(t)
    }

  override def tailRecM[A, B](a: A)(f: A => Try[Either[A, B]]) = ???
}

//

def executeRequestMEThrowable[F[_]](request: HttpRequest)(implicit ME: MonadError[F, Throwable]): F[HttpResponse] =
  try {
    ME.pure(doRequest(request))
  } catch {
    case e: Exception => ME.raiseError(e)
  }

executeRequestMEThrowable[Try](HttpRequest(GET, "www.error.com"))


//the above can be generalized further
def executeRequestMEGeneral[F[_], E](request: HttpRequest)(f: Exception => E)(implicit ME: MonadError[F, E]): F[HttpResponse] =
  try {
    ME.pure(doRequest(request))
  } catch {
    case  e: Exception => ME.raiseError(f(e))
  }

executeRequestMEGeneral[Option, Unit](HttpRequest(GET, "www.err.io"))((e: Exception) => ())


//errors related to enviroment
type ErrorOr[A] = Either[String, A]
//executeRequestMEGeneral[ErrorOr, String](HttpRequest(GET, "www.problem.abc"))((e: Exception) => e.getMessage)


// Functionality that come with the typeclass
MonadError[Option, Unit].attempt(Some(5))
MonadError[Option, Unit].attempt(None)
MonadError[Try, Throwable].attempt(Success(10))
MonadError[Try, Throwable].attempt(Failure(new Exception("oh noes")))

MonadError[Option, Unit].ensure(Some(3))(())(_ % 2 == 0)
MonadError[Option, Unit].ensure(Some(4))(())(_ % 2 == 0)
MonadError[Try, Throwable].ensure(Success(10))(new Exception("oh noes"))(_ * 2 > 18 )
MonadError[Try, Throwable].ensure(Success(10))(new Exception("oh noes"))(_ * 22  > 24 * 10 * 10)
