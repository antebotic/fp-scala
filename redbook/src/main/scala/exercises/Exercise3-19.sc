// Write a function filter that removes elements from a list unless they satisfy a given predicate
// Use it to remove all odd numbers from a List[Int]

def filter[A](as: List[A])(f: A => Boolean): List[A] = {
  def go(ol: List[A], nl: List[A]): List[A] =
    ol match {
      case Nil      => nl
      case hd :: tl => if(f(hd)) go(tl, nl :+ hd) else go(tl, nl)
    }

  go(as, List.empty[A])
}

val nel = (1 to 10).toList
val nelEven = (2 to 10 by 2).toList

filter(nel)(_ % 2 == 0) == nelEven // true


// in terms of foldRight

def foldRight[A, B](la: List[A], z: B)(f: (A, B) => B): B =
  la match {
    case Nil    => z
    case hd::tl => f(hd, foldRight(tl, z)(f))
  }

foldRight(nel, List.empty[Int])((a, b) =>
  if(a % 2 == 0)  a +: b
  else            b
)
