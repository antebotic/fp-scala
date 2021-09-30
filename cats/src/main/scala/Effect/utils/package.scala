package Effect

import cats.effect.IO

package object utils {
  implicit class DebugWrapper[A](ioa: IO[A]){
    def debug: IO[A] =
      ioa.flatMap { a =>
        println(s"Thread [${Thread.currentThread.getName}] - $a")
        IO.pure(a)
      }
  }
}
