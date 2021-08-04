import cats._

object TypeclassesIntOption{

  val s: Semigroup[Int] = new Semigroup[Int]{
    override def combine(x: Int, y: Int): Int =
      x + y
  }

  val mn: Monoid[Int] = new Monoid[Int] {
    override def empty: Int = 0

    override def combine(x: Int, y: Int): Int =
      x + y
  }

  val f: Functor[Option] = new Functor[Option] {
    override def map[A, B](fa: Option[A])(f: A => B): Option[B] =
      fa match {
        case None     =>  None[B]
        case Some(a)  =>  Option(f(a))
      }
  }

  val a: Applicative[Option] = new Applicative[Option] {
    override def pure[A](x: A): Option[A] = Some(x)

    override def ap[A, B](ff: Option[A => B])(fa: Option[A]): Option[B] =
      (ff, fa) match {
        case (Some(f), Some(a)) => Some(f(a))
        case (_, _)             => None[B]
      }
  }

  val m: Monad[Option] = new Monad[Option] {
    def pure[A](x: A): Option[A] = Some(x)

    def flatMap[A, B](a: Option[A])(f: A => Option[B]): Option[B] =
      a match {
        case None     => None[B]
        case Some(a)  => f(a)
      }

    def tailRecM[A, B](a: A)(f: A => Option[Either[A, B]]): Option[B] =
      f(a) match {
        case None               => None[B]
        case Some(Left(nextA))  => tailRecM(nextA)(f)
        case Some(Right(b))     => Some(b)
      }
  }

  val fld: Foldable[Option] = new Foldable[Option] {
    override def foldLeft[A, B](fa: Option[A], b: B)(f: (B, A) => B): B =
      fa match {
        case None     => b
        case Some(a)  => f(b, a)
      }

    //not so sure about this one
    override def foldRight[A, B](fa: Option[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa match {
        case None    => lb
        case Some(a) => Eval.defer(f(a, lb))
      }
  }

}

object TypeClassStringList {

  val sg: Semigroup[String] = new Semigroup[String] {
    override def combine(x: String, y: String): String =
      x concat y
  }

  val mn: Monoid[String] = new Monoid[String] {
    override def empty: String = ""

    override def combine(x: String, y: String): String =
      x concat y
  }

  val f: Functor[List] = new Functor[List] {
    override def map[A, B](fa: List[A])(f: A => B): List[B] =
      fa match {
        case Nil        => Nil
        case hd :: tail => f(hd) +: map(tail)(f)
      }
  }

  //review this, probably not the way to go
  val aplic: Applicative[List] = new Applicative[List] {
    override def pure[A](x: A): List[A] = List(x)

    override def ap[A, B](ff: List[A => B])(fa: List[A]): List[B] =
      (ff, fa) match {
        case (_, _)                 => Nil
        case (h :: t, hd :: tl) => h(hd) +: ap(t)(tl)
      }
  }

  val fld: Foldable[List] = new Foldable[List] {
    override def foldLeft[A, B](fa: List[A], b: B)(f: (B, A) => B): B =
      fa match {
        case Nil        => b
        case hd :: tl   => foldLeft(tl, f(b, hd))(f)
      }

    override def foldRight[A, B](fa: List[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = {
      def go(as: List[A] ): Eval[B] = {
        as match {
          case Nil        => lb
          case hd :: tl  => f(hd, Eval.defer(go(tl)))
        }
      }
      Eval.defer(go(fa))
    }
  }
}
