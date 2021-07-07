import cats._
import cats.implicits._

import scala.util.{Success, Failure}
//import scala.annotation.tailrec

class Example[F[_]: Foldable, A](l: F[A])(implicit m: Monoid[A]){

  val fld = l.foldLeft(m.empty)((x, y) => m.combine(x,y))

}

val li1 = List(1, 2, 3, 4, 5, 6 ,7)
val e1 = new Example[List, Int](li1: List[Int])
e1.fld

/////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////

class Example2[F[_]: Foldable, A](lst: List[Int]){
  val fmShow = lst.foldMap(_.show)
  val fmDouble = lst.foldMap(n => n * 2)
  val fmToOpt  = lst.foldMap(n => if(n >= 3) Success(n) else Failure(new Exception("boom"))).toOption

}

val e2 = new Example2[List, Int](li1)
e2.fmShow
e2.fmDouble
e2.fmToOpt

/////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////

class Example3[F[_]: Foldable, A](lst: List[A])(implicit m: Monoid[A]){
  val fld = lst.fold _
}

val e3 = new Example3[List, String](List("Marcus", "Aurelius"))
e3.fld

val li2 = List("Marcus", "Aurelius").fold _

/////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////

def findC[A](fa: List[A])(p: A => Boolean): Option[A] = {
  fa match {
    case Nil          => None
    case h :: t       => if(p(h)) Some(h) else findC(t)(p)
  }
}

val li3 = List(10, 20, 30, 40)
val li4 = List(1, 3, 5, 7)
findC(li3)(n => n > 10)
findC(li4)(n => n % 2 == 0)

li3.exists(n => n > 10)
li4.exists(n => n > 10)
