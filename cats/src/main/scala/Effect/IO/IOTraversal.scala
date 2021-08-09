package Effect.IO

import Effect.utils.DebugWrapper
import cats.effect.{IO, IOApp}

import scala.concurrent.Future
import scala.util.Random

object IOTraversal extends IOApp.Simple{

  import scala.concurrent.ExecutionContext.Implicits.global
  def heavyComputation(string: String): Future[Int] = Future {
    Thread.sleep(Random.nextInt(1000))
    string.split(" ").length
  }

  val workload: List[String] = List("I like Cats Effect", "Scala is great", "Carpin all them diems")

  def clunkyFutures() : Unit = {
    //Future[List[Int]] would be hard to obtain from the List[Future[Int]]
    val futures: List[Future[Int]] = workload.map(heavyComputation)
    futures.foreach(_.foreach(println))
  }

  // traverse to the rescue
  import cats.Traverse
  import cats.instances.list._

  val listTraverse = Traverse[List]

  def traverseFuturese(): Unit = {

    val singleFuture: Future[List[Int]] = listTraverse.traverse(workload)(heavyComputation)
    singleFuture.foreach(println)
  }

  def computeAsIO(string: String): IO[Int] = IO {
    Thread.sleep(Random.nextInt(1000))
    string.split( " ").length
  }.debug

  val ios: List[IO[Int]] = workload.map(computeAsIO)
  val singleIO: IO[List[Int]] = listTraverse.traverse(workload)(computeAsIO)

  // parallel traversal
  import cats.syntax.parallel._

  val parallelSingleIO: IO[List[Int]] = workload.parTraverse(computeAsIO)

  /**
   * Exercises
   *
   */
  //1
  def sequence[A](listOfIOs: List[IO[A]]): IO[List[A]] =
    listTraverse.traverse(listOfIOs)(identity)

  //2 hard version
  def sequence_v2[F[_]: Traverse, A](wrapperOfIOs: F[IO[A]]): IO[F[A]] =
    Traverse[F].traverse(wrapperOfIOs)(identity)

  // 3 parallel version
  def parSequence[A](listOfIOs: List[IO[A]]): IO[List[A]] =
    listOfIOs.parTraverse(identity)

  // 4 hard version
  def parSequence_v2[F[_]: Traverse, A](wrapperOfIOs: F[IO[A]]): IO[F[A]] =
    wrapperOfIOs.parTraverse(identity)

  // existing sequence API
  val singleIO_v2: IO[List[Int]] = listTraverse.sequence(ios)
  val parallelSingleIO_v2: IO[List[Int]] = parSequence(ios)
  val parallelSingleIO_v3: IO[List[Int]] = ios.parSequence

  override def run: IO[Unit] = {
    singleIO
      .map(_.sum)
      .debug
      .void

    parallelSingleIO
      .map(_.sum)
      .debug
      .void
  }
}
