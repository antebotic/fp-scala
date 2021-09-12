// Can product implemented using foldRight immediately halt the recursion and return 0.0
// Why or why not, to be considered in more detail in chapter 5

def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B =
  as match {
    case  Nil     => z
    case  hd::tl  => f(hd, foldRight(tl, z)(f))
  }


val nel = List(1, 2, 3, 4, 5, 6)
foldRight(nel, 0)(_ + _)

// product
foldRight(nel, 1)(_ * _)

//shortCircuit product if 0.0 if encountered
val nel2 = List(1, 2, 3, 4, 0, 1 ,5, 6, 7)
foldRight(nel2, 1)((a, b) => if (a*b != 0) a * b else -1 )
// This returns -24, meaning the recursion cannot be broken early, meaning placing a 0
// instead of -1 does not change anything, the recursion will not be broken

// foldRight pushes frames on the stack as it goes, all the way to the end of the
// list, before it can begin collapsing them
