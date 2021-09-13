// Compute the length of a list using foldRight

def foldRight[A, B](la: List[A], z: B)(f: (A, B) => B): B =
  la match {
    case Nil    => z
    case hd::tl => f(hd, foldRight(tl, z)(f))
  }


val nel = (1 to 199).toList

foldRight(nel, 0)((a, b) => b + 1) == nel.length // true
