// Write a function that reverses a List. Try to implement using a fold

def foldRight[A, B](la: List[A], z: B)(f: (A, B) => B): B =
  la match {
    case Nil      => z
    case hd :: tl => f(hd, foldRight(tl, z)(f))
  }

def foldLeft[A, B](la: List[A], z: B)(f: (B, A) => B): B =
  la match {
    case Nil      => z
    case hd :: tl => foldLeft(tl, f(z, hd))(f)
  }


val nel = (1 to 5).toList

val inv1 = foldRight(nel, Nil: List[Int])((a, b) => b :+ a)
val inv2 = foldLeft(nel, Nil: List[Int])((a, b) => b +: a)

inv1 == nel.reverse //true
inv2 == nel.reverse //true

