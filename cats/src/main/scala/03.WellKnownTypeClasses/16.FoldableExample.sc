import cats._

import scala.annotation.tailrec

sealed trait MList[+A]

final case class MCons[+A](hd: A, tl: MList[A]) extends MList[A]
case object MNil extends MList[Nothing]


object MList {
  def apply[A](elems: A*): MList[A] ={
    elems.foldRight(mnil[A])((a, b) => mcons(a, b))
  }

  def mnil[A]: MList[A] = MNil
  def mcons[A](hd: A, tl: MList[A]): MList[A] = MCons(hd, tl)

  implicit val listFoldable: Foldable[MList] = new Foldable[MList] {
    @tailrec
    override def foldLeft[A, B](fa: MList[A], b: B)(f: (B, A) => B): B =
      fa match {
        case MNil         => b
        case MCons(h, t)  => foldLeft(t, f(b, h))(f)
      }

    override def foldRight[A, B](fa: MList[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = {
      def loop(as: MList[A]): Eval[B] = {
        as match {
          case MNil         => lb
          case MCons(h, t)  => f(h, Eval.defer(loop(t)))
        }
      }
      Eval.defer(loop(fa))
    }
  }
}



import MList._

val ml = MList(1, 2, 3, -1, -2, -3)

def sum(ints: MList[Int]): Int =
  Foldable[MList].foldLeft(ints, 0)((b, a) => b+a)

def length(ints: MList[Int]): Int =
  Foldable[MList].foldLeft(ints, 0)((b, _) => b + 1)

def filterPositive(ints: MList[Int]): MList[Int] =
  Foldable[MList].foldLeft(ints, mnil[Int])((b, a) => if(a > 0) mcons(a, b) else b)

def filterPositiveFR(ints: MList[Int]): MList[Int] =
  Foldable[MList].foldRight(ints, Eval.now(mnil[Int]))((i, eis) => if(i > 0) Eval.now(mcons(i, eis.value)) else eis).value

sum(ml)
length(ml)
filterPositive(ml)
filterPositiveFR(ml)
