// Implement the function tail for removing the first element of a List
// Built in List Nil.tail throws an exception

def tail[A](la: List[A]): List[A] =
  la match {
    case Nil    => List.empty
    case h :: t => t
  }

tail(List(1,2,3))
tail(List.empty)

// The check is performed within the case object Nil if tail is called on Nil directly
def tail2[A](la: List[A]) : List[A] =
  la match {
    case Nil  => throw new UnsupportedOperationException("tail of empty list")
    case h::t => t
  }

tail2(List(1,2,3))
tail2(List.empty)


// If not called on Nil directly, but instead on an empty list
// drop needs to be implemented

//def tail3[A](la: List[A]): List[A] =
//  if(la.isEmpty) throw new UnsupportedOperationException("tail of empty list")
//  else drop(1)

// tail3(List(1, 2, 3))
//tail3(List.empty)
