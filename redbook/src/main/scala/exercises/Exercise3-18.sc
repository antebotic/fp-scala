// Write a function map that generalizes modifying each element in a list
// while maintaining the structure of the list

def map[A, B](la: List[A])(f: A => B): List[B] = {
  def go(ol: List[A], nl: List[B]): List[B] =
    ol match {
      case Nil        => nl
      case hd :: tl   => go(tl, nl :+ f(hd))
    }

  go(la, List.empty[B])
}


val nel = (1 to 5).toList

map(nel)(_ * 2)


