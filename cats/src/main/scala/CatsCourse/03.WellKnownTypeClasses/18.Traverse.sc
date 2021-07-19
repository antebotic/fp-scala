import cats._
import cats.implicits._

trait MList[+A]

object MList {
  case class MCons[+A](hd: A, tl: MList[A]) extends MList[A]
  case object MNil extends MList[Nothing]

  def apply[A](elems: A*): MList[A] =
    elems.foldRight(mnil[A])((a, b) => mcons(a, b))


  def mnil[A]: MList[A] = MNil
  def mcons[A](hd: A, tl: MList[A]) = MCons(hd, tl)

  // 1. Write a functor instance for MList
  implicit val functorMList: Functor[MList] = new Functor[MList] {
    override def map[A, B](fa: MList[A])(f: A => B): MList[B] =
      fa match {
        case MNil         => MNil
        case MCons(h, t)  => mcons(f(h), map(t)(f))
      }
  }


  // 2. Implement traverse in terms of sequence and using a functor
  //   sequence(fa.map(f))

  implicit val listTraverse: Traverse[MList] = new Traverse[MList] {
    override def traverse[G[_], A, B](fa: MList[A])(f: A => G[B])(implicit evidence$1: Applicative[G]): G[MList[B]] = {
      fa match {
        case MNil         => Applicative[G].pure(MNil)
        case MCons(h, t)  => (f(h), traverse(t)(f)).mapN(MCons.apply)
      }

    }

    override def foldLeft[A, B](fa: MList[A], b: B)(f: (B, A) => B): B =
      fa match {
        case MNil => b
        case MCons(h, t) => foldLeft(t, f(b, h))(f)
      }

    override def foldRight[A, B](fa: MList[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = {
      def loop(as: MList[A]): Eval[B] = {
        as match {
          case MNil         => lb
          case MCons(h, t)  => f(h, Eval.defer(loop(t)))
        }
      }
      Eval.defer(loop(fa))
    }

    override def sequence[G[_], A](fga: MList[G[A]])(implicit G: Applicative[G]): G[MList[A]] =
      traverse(fga)(identity)

  }
}


Traverse[MList].sequence(MList(Option(5), Option(4)))
Traverse[MList].sequence(MList(Option(5), Option(4), None))
Traverse[MList].traverse(MList(1, 2, 3, 4))(i => Option(i + 1))


val optionTraverse: Traverse[Option] = new Traverse[Option] {
  override def traverse[G[_], A, B](fa: Option[A])(f: A => G[B])(implicit G: Applicative[G]): G[Option[B]] =
    fa match {
      case None     => G.pure(None) // Applicative[G].pure(None)
      case Some(a)  => f(a).map(x => Some(x)) // or f(a).map(Some.apply)
    }

  override def foldLeft[A, B](fa: Option[A], b: B)(f: (B, A) => B): B =
    fa match {
      case None     => b
      case Some(a)  => f(b, a)
    }

  override def foldRight[A, B](fa: Option[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
    fa match {
      case None     => lb
      case Some(a)  => f(a, lb)
    }
}


optionTraverse.traverse(Some(5))(x => List(x + 1, x + 2))
optionTraverse.traverse(Some(5))(x => List())
optionTraverse.traverse[List, Int, Int](None)(x => List(x * 1, x * 2))
