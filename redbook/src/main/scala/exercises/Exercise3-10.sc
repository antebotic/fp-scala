// The implementation of foldRight from previous examples is not stack safe
// Write another general list recursion function foldLeft

def foldLeft[A, B](as: List[A], z: B)(f: (B, A) => B): B =
  as match {
    case Nil      => z
    case hd :: tl => foldLeft(tl, f(z, hd))(f)
  }


val nel = (1 to 5).toList

foldLeft(nel, 0)(_ + _) == (1 to 5).reduce(_ + _) // true
