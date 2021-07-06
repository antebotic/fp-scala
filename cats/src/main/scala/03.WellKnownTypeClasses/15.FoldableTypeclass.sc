import cats._
import cats.kernel.laws._



trait Foldable[F[_]]{
  def foldLeft[A, B](fa: F[A], b: B)(f: (B, A) => B): B
  def foldRight[A, B](fa: F[A], lb: Eval[B])(f: (A, Eval[B] => Eval[B])): Eval[B]
}


class Example[F[_]]{
  def foldMap[A, B](fa: F[A])(f: A => B)(implicit M: Monoid[B]): B =
  //foldLeft(fa, M.empty)((b, a) => M.combine(b, f(a)))
  ???


  //Laws
  def leftFoldConsistencyWithFoldMap[A, B](
    fa: F[A],
    f: A => B
  )(implicit M: Monoid[B]): IsEq[B] =
//    fa.foldMap(f) <-> fa.foldLeft(M.Empty){ (b, a) =>
//      b |+| f(a)
//    }
    ???

  def rightFoldConsistencyWithFoldMap[A, B](
    fa: F[A],
    f: A => B
  )(implicit M: Monoid[B]): IsEq[B] =
//    fa.foldMap(f) <-> fa.foldRight(
//      Later(M.empty)
//    )((a, lb) => lb.map(f(a) |+| _)).value
  ???
}

