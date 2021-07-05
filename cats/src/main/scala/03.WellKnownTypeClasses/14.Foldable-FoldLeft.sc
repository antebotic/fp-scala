sealed trait MList[+A]

case class MCons[A](hd: A, tl:MList[A]) extends MList[A]
case object MNil extends MList[Nothing]

def foldLeft[A, B](list: MList[A])(acc: B)(f: (B, A) => B): B =
  list match {
    case MNil => acc
    case MCons(h, t) => foldLeft(t)(f(acc, h))(f)
  }

def sum(ints: MList[Int]): Int =
  foldLeft(ints)(0)((y, x) => x + y)

def length[A](list: MList[A]): Int =
  foldLeft(list)(0)((x, y) => 1 + x)

val ml: MCons[Int] = MCons(1, MCons(2, MCons(3, MCons(4, MCons(5, MNil)))))

sum(ml)
length(ml)
foldLeft(ml)(1)(_ * _)
foldLeft(ml)(20)(_ - _)
