// Write a function that turns each Double value of a List to a String

def doubleToString(ld: List[Double]): List[String] = {
  def go(origL: List[Double], newL: List[String]): List[String] =
    origL match {
      case Nil => newL
      case hd::tl => go(tl, newL :+ hd.toString)
    }

  go(ld, List.empty[String])
}

val nel = (1 to 10).toList.map(_.toDouble)

doubleToString(nel)

//in terms of foldRight

def foldRight[A, B](la: List[A], z: B)(f: (A, B) => B): B =
  la match {
    case Nil      => z
    case hd :: tl => f(hd, foldRight(tl, z)(f))
  }

foldRight(nel, List.empty[String])((a, b) => a.toString +: b)
