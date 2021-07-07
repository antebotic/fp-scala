import cats._
import cats.implicits._
import cats.kernel.laws.IsEq

trait Monoid[A] extends Semigroup[A]{
  def combine(x: A, y: A): A //inherited from semigroup
  def empty: A
}

//laws
class M[A](S: Monoid[A]){
  def semigroupAssociative[A](x: A, y:A, z: A): IsEq[A] =
    IsEq(S.combine(S.combine(x, y), z), S.combine(x, S.combine(y, z)))

  def leftIdentity[A](x: A): IsEq[A] =
    IsEq(S.combine(S.empty, x), x)

  def rightIdentity[A](x: A): IsEq[A] =
    IsEq(S.combine(x, S.empty), x)
}






