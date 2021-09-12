import cats.Monoid
// What is the result of the following match expression

sealed trait MyList[+A]
case class Cons[+A](head: A, tail: MyList[A]) extends MyList[A]
case object Nil extends MyList[Nothing]

object MList {
  def apply[A](as: A*): MyList[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  def sum(la: MyList[Int]): Int = {
    def go(acc: Int, ml: MyList[Int]): Int =
      ml match {
        case Nil        => acc
        case Cons(h, t) => go(acc + h, t)
      }
    go(0, la)
  }
}

val x = MList(1, 2, 3, 4, 5) match {
  case Cons(x, Cons(2, Cons(4, _)))           => x
  case Nil                                    => 42
  case Cons(x, Cons(y, Cons(3, Cons(4, _))))  => x + y // returns 3
  case Cons(h, t)                             => h + MList.sum(t)
  case _                                      => 101
}
