// Implement flatMap
// List(1,2,3) => List(List(1, 1), List(2, 2), List(3,3)) => List(1,1,2,2,3,3)

def flatMap[A, B](ls: List[A])(f: A => List[B]): List[B] ={
  def go(ol: List[A], nll: List[List[B]]):List[B] =
    ol match {
      case Nil      => nll.flatten
      case hd :: tl => go(tl, nll :+ f(hd))
    }

  go(ls, List(List.empty[B]))
}

val nel = List(1, 2, 3)

flatMap(nel)(i => List(i, i))

//Using concat
def foldRight[A, B](la: List[A], z: B)(f: (A, B) => B): B =
  la match {
    case Nil => z
    case hd::tl => f(hd, foldRight(tl, z)(f))
  }

def concat[A](l: List[List[A]]): List[A] =
  foldRight(l, List.empty[A])(_ ::: _)

def map[A, B](la: List[A])(f: A => B): List[B] =
  foldRight(la, List.empty[B])((a,b) => f(a) +: b)

def flatMap_v2[A, B](la: List[A])(f: A => List[B]): List[B] =
  concat(map(la)(f))

flatMap_v2(nel)(i => List(i, i, i))


