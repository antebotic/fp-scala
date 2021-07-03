/*
  Laws

  1. Monad left identity
    def monadLeftIdentity[A, B](a: A, f: A => F[B]): IsEq[F[B]] =
      F.pure(a).flatMap(f) <-> f(a)

  2. Monad right identity
    def monadRightIdentity[A, B](fa: F[A]) : IsEq[F[A]] =
      fa.flatMap(F.pure) <-> fa

  3. Flatmap associativity
    def flatMapAssociativity[A, B, C](fa: F[A], f: A => F[B], g: B => F[C]): IsEq[F[C]] =
      fa.flatMap(f).flatMap(g) <-> fa.flatMap(a => f(a).flatMap(g))
 */

import cats._
import cats.implicits._

sealed trait MOption[+A]

object MOption{
  final case class MSome[A](a: A) extends MOption[A]
  case object MNone extends MOption[Nothing]

  implicit val monadMOption: Monad[MOption] = new Monad[MOption]{
    override def flatMap[A, B](fa: MOption[A])(f: A => MOption[B]): MOption[B] =
      fa match{
        case MSome(value) => f(value)
        case MNone        => MNone
      }

    override def tailRecM[A, B](a: A)(f: A => MOption[Either[A, B]]): MOption[B] = ???

    override def pure[A](x: A): MOption[A] = MSome(x)

    override def map[A, B](fa: MOption[A])(f: A => B): MOption[B] =
      flatMap(fa)(a => pure(f(a)))

    override def flatten[A](ffa: MOption[MOption[A]]): MOption[A] = {
    flatMap(ffa)(fa => fa)
    // also possible -> flatMap(identity)
    }
  }
}

import MOption._
val x: MOption[Int] = Monad[MOption].pure(5)
val y: MOption[Int] = Monad[MOption].pure(6).flatMap(n => Monad[MOption].pure(n + 1))
val n: MOption[Int] = (MNone: MOption[Int]).flatMap(n => Monad[MOption].pure(n + 1))
val z: MOption[Int] = Monad[MOption].pure(22).flatMap(n => MNone)

val f : MOption[Int] = for {
  a <- Monad[MOption].pure(21)
  b <- Monad[MOption].pure(19)
} yield a - b

val k : MOption[Int] = for {
  a <- Monad[MOption].pure(21)
  b <- (MNone: MOption[Int])
} yield a - b

val ffa: MOption[MOption[Int]] = Monad[MOption].pure(Monad[MOption].pure(44))
ffa.flatten