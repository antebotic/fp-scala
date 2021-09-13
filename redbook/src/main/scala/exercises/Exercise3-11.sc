// Differences between foldRight and foldLeft

def foldRight[A, B](as: List[A], acc: B)(f: (A, B) => B ): B = {
  as match {
    case Nil      => acc
    case hd :: tl => f(hd, foldRight(tl, acc)(f))

  }
}

def foldLeft[A, B](as: List[A], acc: B)(f: (B, A) => B): B =
  as match {
    case Nil      => acc
    case hd :: tl => foldLeft(tl, f(acc, hd))(f)
  }

// Implement product, sum and length List functions using foldLeft

def sum(li: List[Int]) =
  foldLeft(li, 0)(_ + _)

def product(li: List[Int]) =
  foldLeft(li, 1)(_ * _)

def length(li: List[Int]) =
  foldLeft(li, 0)((len, _) => len + 1)


val nel = (1 to 5).toList

sum(nel)

product(nel)

length(nel)
