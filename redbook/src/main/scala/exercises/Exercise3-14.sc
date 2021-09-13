// Implement append in terms of foldRight or foldLeft

def foldRight[A, B](la: List[A], acc: B)(f: (A, B) => B): B =
  la match {
    case Nil    => acc
    case hd::tl => f(hd, foldRight(tl, acc)(f))
  }

def foldLeft[A, B](la: List[A], acc: B)(f: (B, A) => B): B =
  la match {
    case Nil    => acc
    case hd::tl => foldLeft(tl, f(acc, hd))(f)
  }


// foldLeft might not be suitable
def appendFl(la: List[Int], el: List[Int]): List[Int] =
  foldLeft(la, el)((a, b) => b +: a) // List(5, 4, 3, 2, 1, 6)

def appendFr(la: List[Int], el: List[Int]): List[Int] =
  foldRight(la, el)(_ :: _)

val nel = (1 to 5).toList

appendFl(nel, List(6))
appendFr(nel, List(6))
