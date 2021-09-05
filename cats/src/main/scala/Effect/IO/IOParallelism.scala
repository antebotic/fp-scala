package Effect.IO

import cats.effect.{IO, IOApp}

object IOParallelism extends IOApp.Simple {
  // IOs are usually sequential
  val firstIOThread = IO(s"${Thread.currentThread().getName}")
  val secondIOThread = IO(s"${Thread.currentThread().getName}")

  // run on same thread
  val composedIO = for {
    first   <- firstIOThread
    second  <- secondIOThread
  } yield s"$first and $second"

  // debug extension method
  import Effect.utils._
  import cats.syntax.apply._

  val favLang: IO[String]     = IO.delay("Scala").debug
  val meaningOfLife: IO[Int]  = IO.delay(42).debug

  val goalInLife =
    (favLang, meaningOfLife)
      .mapN((num, str) => s"The num is $num, and the str is $str")

  // parallelism on IOs
  // convert sequential IO to parallel IO
  import cats.Parallel

  val parIO1: IO.Par[Int]     = Parallel[IO].parallel(meaningOfLife)
  val parIO2: IO.Par[String]  = Parallel[IO].parallel(favLang)

  import cats.effect.implicits._

  val goalInLifePar: IO.Par[String] =
    (parIO1, parIO2)
      .mapN((num, str) => s"The num is $num, and the str is $str")

  val goalInLife_v2: IO[String] = Parallel[IO].sequential(goalInLifePar)

  // shorthand for parallel map, does the conversion from par to seq automagically
  import cats.syntax.parallel._

  val goalInLife_v3: IO[String] =
    (meaningOfLife, favLang)
      .parMapN((num, str) => s"The num is $num, and the str is $str")

  // failures
  val aFailure: IO[String] = IO.raiseError(new RuntimeException("This is too much"))

  val parFailure =
    (meaningOfLife, aFailure)
      .parMapN(_ + _)

  // compose two failures, non deterministic outcome
  val anotherFailure: IO[String] = IO.raiseError(new RuntimeException("Second failure"))

  val twoFailures: IO[String] =
    (aFailure, anotherFailure)
      .parMapN(_ + _)

  override def run: IO[Unit] = {
    import scala.io.StdIn
    import scala.util.{Try, Success, Failure}

    val availableEffects: IO[Unit] = IO.delay(
      println("Select effect to be ran(1 - 6):")
    )
    //maybe rewrite this using redeemWith
    def pickEffect: IO[Int] = {
      val choice    = StdIn.readLine()
      Try(choice.toInt) match {
        case Failure(exception) => {
          println(s"Invalid input, please use a number from 1 to 6, Parse error ${exception.getMessage}")
          pickEffect
        }
        case Success(number) => IO.pure(number)
      }
    }

    def runSelected(selected: IO[Int]): IO[Unit] =
      selected.flatMap(
        _ match {
          case 1 => composedIO.map(println)   // run in sequence, on the same thread
          case 2 => goalInLife.map(println)   // run in sequence, on the same thread
          case 3 => goalInLife_v2.debug.void  // runs in parallel, on different threads
          case 4 => goalInLife_v3.debug.void  // runs in parallel, on different threads
          case 5 => parFailure.debug.void     // throws
          case 6 => twoFailures.debug.void    // throws
          case _ => IO("Sorry")
        }
      )

    for {
      _       <- availableEffects
      choice  <- pickEffect
      _       <- runSelected(IO.pure(choice))
    } yield ()

  }
}
