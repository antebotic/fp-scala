import cats._
import cats.implicits._

// * -> * -> * represents Either, using Either[E, *]] reduces it to * -> *
// Either is right biased in cats, left is indicating an error

object MonadEither{
  implicit def eitherMonad[E]: Monad[Either[E, *]] = new Monad[Either[E, *]] {
    override def pure[A](x: A) = Right(x)

    override def flatMap[A, B](fa: Either[E, A])(f: A => Either[E, B]): Either[E, B] =
      fa match {
        case Left(lv) => lv.asLeft[B]
        case Right(rv) => f(rv)
      }

    override def tailRecM[A, B](a: A)(f: A => Either[E, Either[A, B]]): Either[E, B] = ???
  }
 }

//Either should fail fast
val x: Either[String, Int] = 5.asRight[String].flatMap(i => (i+1).asRight[String])
val y: Either[String, Int] = 5.asRight[String]
  .flatMap(_ => "boom".asLeft[Int])
  .flatMap(_ => "boom2".asLeft[Int]) // fail fast makes this never run
