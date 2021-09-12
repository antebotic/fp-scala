// What happens when you pass Nil and Cons themselves to the foldRight

def foldRight[A, B](la: List[A], z: B)(f: (A, B) => B): B = {
  la match {
    case Nil    => z
    case hd::tl => f(hd, foldRight(tl, z)(f))
  }

}

foldRight(List(1,2,3), Nil: List[Int])(::(_ , _))         // List(1, 2, 3)
foldRight(List(1,2,3), Nil: List[Int])((a, b) => a :: b)  // List(1, 2, 3)
// What does this say of the relationship between foldRight and List data constructors?
// Fold right can be used to construct a list provided the sequence is not empty, and
// the fold right accumulator element is Nil
