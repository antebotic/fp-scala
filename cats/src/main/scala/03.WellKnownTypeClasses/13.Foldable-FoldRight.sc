trait MList[+A]

case class MCons[+A](hd: A, tl: MList[A]) extends MList[A]
case object MNil extends MList[Nothing]

def sum(ints: MList[Int]): Int =
  ints match {
    case MNil => 0
    case MCons(hd, tl) => hd + sum(tl)
  }

def length[A](list: MList[A]): Int =
  list match {
    case MNil          => 0
    case MCons(_, tl) =>  1 + length(tl)
  }

def filterPositive(ints: MList[Int]): MList[Int] =
  ints match {
    case MNil           => MNil
    case MCons(hd, tl)  =>
      if (hd > 0) MCons(hd, filterPositive(tl))
      else        filterPositive(tl)
  }

def foldRight[A, B](list: MList[A])(acc: B)(f: (A, B) => B): B =
  list match {
    case MNil  => acc
    case MCons(h, t) => f(h, foldRight(t)(acc)(f))
  }

def sumFR(ints: MList[Int]): Int =
  foldRight(ints)(0)(_ + _)

def lengthFR[A](list: MList[A]): Int =
  foldRight(list)(0)((_, y) => 1 + y)

//  def filterPositiveFR(ints: MList[Int]): MList[Int] =
//    foldRight(ints)(Nil)((x, y) => if(x > 0) MCons(x, y) else y)
