package Effect.IO

import cats.effect.IO

import scala.util.{Try, Success, Failure}

object IOErrorHandling {
  // IO up to now: pure, delay, defer
  // Creating failed effects

  val aFailedCompute: IO[Int] = IO.delay(throw new RuntimeException("A failure"))
  val aFailure: IO[Int] = IO.raiseError(new RuntimeException("a proper fail"))

  // handling exceptions
  val dealWith = aFailure.handleErrorWith {
    case _: RuntimeException => IO.delay(println("I am still here"))
  }

  val effectAsEither: IO[Either[Throwable, Int]] = aFailure.attempt

  // redeem: transform the failure and the success in one go
  val resultAsString: IO[String] =
    aFailure.redeem(ex => s"FAIL: $ex", value => s"SUCCESS: $value")

  // redeemWith
  val resultAsEffect: IO[Unit] =
    aFailure.redeemWith(ex => IO(println(s"FAIL: $ex")), value => IO(println(s"SUCCESS: $value")))

  //Exercises

  // 1 - construct potentialy failed IOs from standard data types: Option, Try, Either
  def option2IO[A](optA: Option[A])(ifEmpty: Throwable): IO[A] =
    optA match {
      case None     => IO.raiseError(ifEmpty)
      case Some(a)  => IO.pure(a)
    }

  def try2IO[A](aTry: Try[A]): IO[A] =
   aTry match {
     case Success(a)          => IO.pure(a)
     case Failure(exception)  => IO.raiseError(exception)
   }

  def either2IO[A](anEither: Either[Throwable, A]): IO[A] =
    anEither match {
      case Left(throwable)  => IO.raiseError(throwable)
      case Right(a)         => IO.pure(a)
    }

  // 2 - handleError, handleErrorWith
  def handleIOError[A](ioa: IO[A])(handler: Throwable => A): IO[A] =
    ioa.redeem(handler, identity) // identity is the same as a => a

  def handleIOErrorWith[A](ioa: IO[A])(handler: Throwable => IO[A]): IO[A] =
    ioa.redeemWith(handler, IO.pure) // IO.pure is the same as: a => IO.pure(a)


  def main(args: Array[String]): Unit ={
    import cats.effect.unsafe.implicits.global
    // aFailedCompute.unsafeRunSync() // throws
    // aFailure.unsafeRunSync()       // throws
    dealWith.unsafeRunSync()          // safe
    println(resultAsString.unsafeRunSync())
  }
}
