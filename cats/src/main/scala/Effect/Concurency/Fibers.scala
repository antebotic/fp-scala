package Effect.Concurency

import Effect.utils._
import cats.effect.kernel.Outcome.{Canceled, Errored, Succeeded}
import cats.effect.{Fiber, IO, IOApp, Outcome}
import scala.concurrent.duration._

object Fibers extends IOApp.Simple{

  val meaningOfLife = IO.pure(42)
  val favLang       = IO.pure("Scala")

  // runs on the same thread
  def simpleIOComposition = for {
    _ <- meaningOfLife.debug
    _ <- favLang.debug
  } yield ()

  // Fiber primitive is similar to a thread, but uses much less resources
  // Runs on a thread scheduled and operated by the CatsEffect runtime
  // Incredibly lightweight

  def createFiber: Fiber[IO, Throwable, String] = ??? // almost imposible to create fibers manually

  // the allocation of a fiber is an effectfull operation, and needs to be wrappedin an effectfull data structure
  val aFiber: IO[Fiber[IO, Throwable, Int]] = meaningOfLife.debug.start

  // evaluates on a different thread
  def differentThreadIoOs() = for {
  _ <- aFiber
  _ <- favLang.debug
  } yield ()

  // managing th e lifecycle of a fiber, join means wait for the execution to finish
  def runOnSomeOtherThread[A](ioa: IO[A]): IO[Outcome[IO, Throwable, A]] = for {
    fib     <- ioa.start
    result  <- fib.join // an effect which waits for the fiber to terminate
  } yield result

  //IO(Succeeded(IO(42))
  val fiberJoinResult = runOnSomeOtherThread[Int](meaningOfLife)
    .debug
    .void
  /*
    possible outcomes:
      - success with an IO
      - failure with an exception
      - cancelled
  */

  val ex1 = runOnSomeOtherThread(meaningOfLife)
  val ex2 = ex1.flatMap{
    case Succeeded(fa) => fa
    case Errored(e)    => IO(0)
    case Canceled()    => IO(0)
  }

  def throwOnAnotherThread = for {
    fib     <- IO.raiseError[Int](new RuntimeException("no number for you")).start
    result  <- fib.join
  } yield result

  def testCancel: IO[Unit] = {
    val task = IO("starting").debug >> IO.sleep(1.second) >> IO("done").debug
    val taskWithCancelationHandler = task.onCancel(IO("I was canceled").debug.void)

    for {
      fib     <- taskWithCancelationHandler.start // runs on separate thread
      _       <- IO.sleep(500.millis) >> IO("canceling").debug  // runs on calling thread
      _       <- fib.cancel // sends a signal to the CE runtime from the calling thread
      result  <- fib.join
    } yield result
  }

  override def run = testCancel.debug.void
}
