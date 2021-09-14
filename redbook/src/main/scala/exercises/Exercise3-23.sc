// Generalize the function from the previous exercise to accept any parameter
// Name the function zipWith

def zipWith[A, B, C](la: List[A], lb: List[B])(f: (A, B) => C): List[C] =
  (la, lb) match {
    case (_, Nil)       => Nil
    case (Nil, _)       => Nil
    case (h::t, hd::tl) => f(h, hd) +: zipWith(t, tl)(f)
  }

val nel1 = List(1, 2, 3)
val nel2 = List(2, 3, 4)

zipWith(nel1, nel2)(_ + _)
