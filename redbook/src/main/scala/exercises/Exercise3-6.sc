// Implement a function init, that returns a List consisting of all but the last element of a List

def init[A](la: List[A]): List[A] ={
  def go(lx: List[A], ly: List[A]):List[A] = {
    lx match {
      case h :: Nil => ly
      case _        => go(lx.tail, ly :+ lx.head)
    }
  }

  if(la.isEmpty)  la
  else            go(la, List.empty)
}

val el  = List.empty
val nel = List(1, 2, 3, 4, 5, 6, 7)

init(el)
init(nel)
