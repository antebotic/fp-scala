package Effect.IO

import cats.effect.IO

import scala.annotation.tailrec
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

  ////////////////////////////////////////////////////////////
  //////////////////////// Exercises /////////////////////////
  ////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////
  // 1 - Return the second, if the first finishes successfully
  def sequenceTakeLast[A, B](ioa: IO[A], iob: IO[B]): IO[B] =
    ioa.flatMap(_ => iob)

  def sequenceTakeLast_v2[A, B](ioa: IO[A], iob: IO[B]): IO[B] =
    ioa *> iob  //andThen symbol as this is a common operation

  def sequenceTakeLast_v3[A, B](ioa: IO[A], iob: IO[B]): IO[B] =
    ioa >> iob // andThen by name call, evaluation is lazy on this one


  ////////////////////////////////////////////////////////////
  // 2 - Sequence two IOs and take the result of the first one
  def sequenceTakeFirst[A, B](ioa: IO[A], iob: IO[B]): IO[A] =
    ioa.flatMap(a => iob.map(_ => a))

  def sequenceTakeFirst_v2[A, B](ioa: IO[A], iob: IO[B]): IO[A] =
    ioa <* iob


  ////////////////////////////////////////////////////////////
  // 3 - repeat an IO effect forever
  def forever[A](io: IO[A]): IO[A] =
    io.flatMap(_ => forever(io))

  def forever_v2[A](io: IO[A]): IO[A] =
    io >> forever_v2(io) // evaluates lazily

  def forever_v3[A](io: IO[A]): IO[A] =
    io *> forever_v2(io) // evaluates eagerly will stack overflow

  def forever_v4[A](io: IO[A]): IO[A] =
    io.foreverM // with stack recursion


  ////////////////////////////////////////////////////////////
  // 4 - convert an IO to a different type
  def convert[A, B](ioa: IO[A], value: B) : IO[B] =
    ioa.map(_ => value)

  def convert_v2[A, B](ioa: IO[A], value: B): IO[B] =
    ioa.as(value)


  ////////////////////////////////////////////////////////////
  // 5 - discard value inside IO, just return Unit
  def asUnit[A](ioa: IO[A]): IO[Unit] =
    ioa.map(_ => ())

  def asUnit_v2[A](ioa: IO[A]): IO[Unit] =
    ioa.as(()) // not very readable

  def asUnit[A](ioa: IO[A]): IO[Unit] =
    ioa.void // built in method to discard and return unit


  ////////////////////////////////////////////////////////////
  // 6 - fix stack recursion
  def sum(n: Int): Int =
    if(n <= 0) 0
    else n + sum(n - 1)

  // fixed
  def sumTailRec(n: Int): Int = {
    @tailrec
    def sumWithAccumulator(p: Int, accumulator: Int): Int =
      if(n <= 0) accumulator
      else sumWithAccumulator(p, accumulator)

    sumWithAccumulator(n, 0)
  }

  def sumIO(n: Int): IO[Int] = IO(sumTailRec(n))

  def sumIO_fromCourse(n: Int): IO[Int] = {
    if(n <= 0) IO(0)
    else for {
      lastNumber  <- IO(n)
      prevSum     <- sumIO_fromCourse(n - 1)
    } yield lastNumber + prevSum
  }


  ////////////////////////////////////////////////////////////
  // 7 - write fibonnaci IO that does NOT crash on recursion
  def fibonaci(n: Int): IO[BigInt] = IO.delay({
    @tailrec
    def fibTailRec(index: BigInt, prev: BigInt, current: BigInt): BigInt =
      if(index <= 0) current
      else fibTailRec(index -1, prev + current, prev)

    fibTailRec(BigInt(n), 1, 0)
  })

  def fibonacci_fromCourse(n: Int): IO[BigInt] =
    if (n < 2) IO(1)
    else for {
    last <- IO(fibonacci_fromCourse(n - 1)).flatten // also can be written as .flatMAp(x => x), or IO.defer
    prev <- IO.defer(fibonacci_fromCourse(n - 2))   // IO.defer is same as .delay(...).flatten
    } yield last + prev


  def main(args: Array[String]): Unit = {
    import cats.effect.unsafe.implicits.global

    // at the "end of the world" effects described within IO's are run
    // call unsafeRunSync or unsafeRunAsync ideally once at the top level
    println(aDelayedIO.unsafeRunSync())
    println(smallProgram.unsafeRunSync())
    println(smallProgram_v2.unsafeRunSync())

    def sayForeverTwiceASecond: Unit = {
      println("forever")
      Thread.sleep(500)
    }

    forever(IO(sayForeverTwiceASecond)).unsafeRunSync()
    forever_v2(IO(sayForeverTwiceASecond)).unsafeRunSync()

  }
}
