// Implement the function setHead for replacing the rist element of
// a List with a diferent value

def setHead[A](h: A, la: List[A]): List[A] =
  la match {
    case hd :: t => h +: t
    case _       => List(h)
  }

val el = List.empty
val nel = List(1, 2,3,4)

setHead(22, el)
setHead(22, nel)
