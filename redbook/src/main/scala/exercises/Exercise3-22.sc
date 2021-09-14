// Write a function that accepts two lists and constructs a new list by adding corresponding elements
// List(1, 2, 3) List(4, 5, 6) => List(5, 7, 9)

def addCoresponding(la: List[Int], lb: List[Int]): List[Int] = {
  def go(f: List[Int], s: List[Int], acc: List[Int]): List[Int] =
    (f, s) match {
      case (hd::tl, hdd::tll) => go(tl, tll, acc :+ (hd + hdd))
      case (Nil, hdd::tll)    => go(Nil, tll, acc :+ hdd)
      case (hd::tl, Nil)      => go(tl, Nil, acc :+ hd)
      case (Nil, Nil)         => acc
    }

    go(la, lb, List.empty[Int])
  }

val nel1 = List(1, 2, 3)
val nel2 = List(4, 5, 6, 7)

addCoresponding(nel1, nel2) // List(5, 7, 9, 7)
