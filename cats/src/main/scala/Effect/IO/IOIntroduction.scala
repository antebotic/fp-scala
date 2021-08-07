package Effect.IO

import cats.effect.IO

import scala.io.StdIn

object IOIntroduction {
  // pure is used for arguments that should not have side effects
  val ourFirstIO: IO[Int] = IO.pure(42)

  // delay is used for arguments sthat should/could have side effects
  val aDelayedIO: IO[Int] = IO.delay({
    println("I am producing an integer")
    47
  })

  // using pure for an effect will evaluate eagerly
  val dontDoThis: IO[Int] = IO.pure {
    println("Compiler will not warn of this being an effect, it will be run immediately")
    54
  }

  // another way of using .delay is by invoking the apply method of IO
  val aDelayedIO_v2: IO[Int] = IO {
    println("I'am safely delayed")
    87
  }

  // map, flatMap
  val improvedMeaningOfLife = ourFirstIO.map(_ * 2)
  val printedMeaningOfLife  = ourFirstIO.flatMap(mol => IO.delay(println(mol)))

  def smallProgram(): IO[Unit] =
    for {
      line1 <- IO(StdIn.readLine())
      line2 <- IO(StdIn.readLine())
      _     <- IO.delay(println(line1 + line2))
    } yield ()

  // mapN - combine IO effects as tuples
  import cats.syntax.apply._

  val combinedMeaningOfLife: IO[Int] =
    (ourFirstIO, improvedMeaningOfLife).mapN(_ + _)

  val smallProgram_v2: IO[Unit] =
    (IO(StdIn.readLine()), IO(StdIn.readLine()))
      .mapN(_ + _)
      .map(println)

  def main(args: Array[String]): Unit = {
    import cats.effect.unsafe.implicits.global

    // at the "end of the world" effects described within IO's are run
    // call unsafeRunSync or unsafeRunAsync ideally once at the top level
    println(aDelayedIO.unsafeRunSync())
    println(smallProgram.unsafeRunSync())
    println(smallProgram_v2.unsafeRunSync())
  }
}
