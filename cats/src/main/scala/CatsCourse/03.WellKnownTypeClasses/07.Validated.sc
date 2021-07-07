sealed trait Validated1[+A]

final case class Valid1[+A](a: A) extends Validated1[A]

final case class Invalid1(errors: List[String]) extends Validated1[Nothing]

import cats._
import cats.implicits._

sealed trait Validated[+A]

object Validated {
  final case class Valid[+A](a: A) extends Validated[A]
  final case class Invalid(errors: List[String]) extends Validated[Nothing]

  implicit val applicative: Applicative[Validated] = new Applicative[Validated] {
    override def pure[A](x: A): Validated[A] = Valid(x)

    override def ap[A, B] (vf: Validated[A => B])(va: Validated[A]): Validated[B] =
      (vf, va) match {
        case (Valid(f), Valid(b)) => Valid(f(b))
        case (Valid(_), Invalid(b)) => Invalid(b)
        case (Invalid(f), Valid(_)) => Invalid(f)
        case (Invalid(f), Invalid(b)) => Invalid(f ++ b)
      }
        //map2(vf, va)((f, a) => f(a))

    override def map2[A, B, C](va: Validated[A], vb: Validated[B])(f: (A, B) => C): Validated[C] = {
//      (va, vb) match {
//        case (Valid(a), Valid(b)) => Valid(f(a,b))
//        case (Valid(_), Invalid(b)) => Invalid(b)
//        case (Invalid(a), Valid(_)) => Invalid(a)
//        case (Invalid(a), Invalid(b)) => Invalid(a ++ b)
//      }
      val tuppledFunction: A => B => C = a => b => f(a, b) //tupled function can be curried
      val anotherTupFunc: A=>B=>C = f.curried // curries the function the same as above
      ap(ap(pure(f.curried))(va))(vb)
    }

    override def map3[A, B, C, D](va: Validated[A], vb: Validated[B], vc: Validated[C])(f: (A, B, C) => D): Validated[D] =
      ap(ap(ap(pure(f.curried))(va))(vb))(vc)

    def tupled[A, B](va: Validated[A], vb: Validated[B]): Validated[(A, B)] =
      map2(va, vb)((a, b) => (a, b))
  }
}



val v1: Validated[Int] = Applicative[Validated].pure(1)
val v2: Validated[Int] = Applicative[Validated].pure(2)
val v3: Validated[Int] = Applicative[Validated].pure(3)

(v1, v2, v3).mapN((a, b, c) => a + b + c)


val optionApplicative: Applicative[Option] = new Applicative[Option]{
  override def pure[A](x: A): Option[A] = Some(x)

  override def ap[A, B](ff: Option[A => B])(fa: Option[A]): Option[B] =
  (ff, fa) match {
    case (Some(f), Some(a)) => pure(f(a))
    case _                  => None
  }
}


optionApplicative.map2(Some(3), Some(4))(_ + _)
optionApplicative.map2[Int, Int, Int](None, Some(4))(_ + _)


val listApplicative: Applicative[List] = new Applicative[List] {
  override def pure[A](x: A): List[A] = List(x)

  override def ap[A, B](ff: List[A => B])(fa: List[A]): List[B] =
    (ff, fa) match {
      case (f :: fs, a :: as) => (a :: as).fmap(f) ++ ap(fs)(a :: as)
      case _ => Nil
    }

}

listApplicative.map2(List(1, 2, 3), List(4, 5))(_ + _)
listApplicative.map2[Int, Int, Int](List.empty, List(4, 5))(_ + _)