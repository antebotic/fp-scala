// Write a function that transforms a list of integers by adding 1 to each element
// this should be a pure function that returns a new list

def addOne(li: List[Int]): List[Int] = {
  def go(original: List[Int], addOne: List[Int]): List[Int] =
    original match {
      case Nil      => addOne
      case hd::tl   => go(tl, addOne :+ (hd + 1))
    }
  go(li, List.empty)
}

val nel = (1 to 10).toList
addOne(nel)


// in terms of foldRight

def foldRight[A, B](la: List[A], z: B)(f: (A, B) => B): B =
  la match {
    case Nil    => z
    case hd::tl => f(hd, foldRight(tl, z)(f))
  }


foldRight(nel, List.empty[Int])((a, b) => (a + 1) :: b)
