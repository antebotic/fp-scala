// foldRight can also be useful to write a function concat that concatenates a list of lists into
// a single list

def foldRight[A, B](la: List[A], z: B)(f: (A, B) => B):B =
  la match {
    case Nil      => z
    case hd :: tl => f(hd, foldRight(tl, z)(f))
  }

def concat[A](l: List[List[A]]): List[A] =
  foldRight(l, Nil: List[A])((a, b) => a ::: b)

val nestedL = List(List(1, 2), List(3, 4), List(5, 6))

concat(nestedL)

