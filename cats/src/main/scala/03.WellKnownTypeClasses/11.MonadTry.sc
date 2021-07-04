import cats._
import cats.implicits._
import scala.util._

implicit val tryMonad: Monad[Try] = new Monad[Try] {
  override def pure[A](x: A): Try[A] = Success(x)

  override def flatMap[A, B](fa: Try[A])(f: A => Try[B]): Try[B] =
    fa match {
      case Failure(f) => Failure(f)
      case Success(s) => f(s)
    }

  override def tailRecM[A, B](a: A)(f: A => Try[Either[A, B]]): Try[B] = ???
}


tryMonad.pure(5)
tryMonad.pure(5).flatMap(i => tryMonad.pure(i + 1))
tryMonad.pure(5).flatMap(_ => Failure(new Exception("oh noes")))

tryMonad.pure(5)
  .flatMap(_ => Failure(new Exception("oh noes")))
  .flatMap((_: Int) => Failure(new Exception("oh noes times two")))

//Try breaks monadic laws
Success(6).flatMap(n => throw new Exception("failed"))

//pure(x).flatMap(f) === f(x)
val f: Int => Try[Int] = i => Success(i + 1)
Success(10).flatMap(f)

// violates referential transparency
val e: Int => Try[Int] = i => throw new Exception("boom")
Success(10).flatMap(e) //encapsulates error in Failure
e(10) // throws with a stacktrace