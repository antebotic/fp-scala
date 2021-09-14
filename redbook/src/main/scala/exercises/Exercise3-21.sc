// Use flatMap to implement filter

def foldRight[A, B](la: List[A], z: B)(f: (A, B) => B): B =
  la match {
    case Nil      => z
    case hd :: tl => f(hd, foldRight(tl, z)(f))
  }

def concat[A](lla: List[List[A]]): List[A] =
  foldRight(lla, List.empty[A])(_ ::: _)

def map[A, B](la: List[A])(f: A => B): List[B] =
  foldRight(la, List.empty[B])((a, b) => f(a) +: b)

def flatMap[A, B](la: List[A])(f: A => List[B]): List[B] =
  concat(map(la)(f))

def filter[A](la: List[A])(f: A => Boolean): List[A] =
  flatMap(la)(a => if(f(a)) List(a) else Nil)


val nel = (1 to 100).toList
filter(nel)(_ < 20)
