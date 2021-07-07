//
//trait Functor[F[_]]{
//  def map[A, B](fa: F[A])(f: A => B):F[B]
//}

//Laws
//Identity:

// something.map(x => x) <-> something

//Compostion
// something
//  .map(x => toUpper(x))
//  .map(x => toLower(x))

// is the same as
// something.map(toLower(toUpper(x)))

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

import cats._
import cats.implicits._
import cats.kernel.laws.IsEq

class Example[F[_]: Functor]{
 def covariantIdentity[A](fa: F[A]): IsEq[F[A]] =
   IsEq(fa.map(identity), fa)

  def covariantComposition[A, B, C](
    fa: F[A], f: A => B, g: B => C
   ): IsEq[F[C]] =
    IsEq(fa.map(f).map(g), fa.map(f.andThen(g)))
}

class Secret[A](val value: A){
  private def hashed: String = {
    val s = value.toString
    val bytes = s.getBytes(StandardCharsets.UTF_8)
    val d = MessageDigest.getInstance("SHA-1")
    val hashBytes = d.digest(bytes)
    new String(hashBytes, StandardCharsets.UTF_8)
  }

  override def toString: String = hashed
}

object Secret {
  implicit val secretFunctor = new Functor[Secret] {
    override def map[A, B](fa: Secret[A])(f: A => B): Secret[B] =
      new Secret(f(fa.value))
  }
}

val marcusSecret: Secret[String] = new Secret("Marcus Aurelius")
marcusSecret.value

Functor[Secret].map(marcusSecret)(_.toUpperCase)
Functor[Secret].map(marcusSecret)(_.toUpperCase).value

//Exercises
// Define Option functor
// Define List functor

val optionFunctor: Functor[Option] = new Functor[Option] {
  override def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa match {
    case Some(value) => Some(f(value))
    case None        => None
  }
}

val listFunctor: Functor[List] = new Functor[List] {
  override def map[A, B](fa: List[A])(f: A => B): List[B] = fa match {
    case Nil => Nil
    case hd :: tl => f(hd) :: map(tl)(f)
  }
}

optionFunctor.map(Some(3))(_ + 12)
//optionFunctor.map(None)(_ + 21)
listFunctor.map(List(1,2,3))(_ * 2)
listFunctor.as(List(1, 3, 5), 10)
optionFunctor.as(Some(4), 11)