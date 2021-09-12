// Generalize tail to the function drop, which removes the first n elements from the list

def drop[A](la: List[A], n:Int): List[A] = {
  def go(dla: List[A], idx: Int): List[A]={
    if(idx >= n)  dla
    else dla match {
      case Nil        => Nil
      case _ :: tl    => go(tl, idx + 1)
    }
  }

  go(la, 0)
}

drop(List(1, 2, 3, 4, 5), 3)
