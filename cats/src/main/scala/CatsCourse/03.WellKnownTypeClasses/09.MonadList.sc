import cats._
import cats.implicits._

val result =
  for {
    a <- List(1, 2, 3)
    b <- List(4, 5, 6)
  } yield a + b

//Implement this behaviour

val listMonad: Monad[List] = new Monad[List] {
  override def pure[A](x: A): List[A] = List(x)

  override def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] =
    fa match {
      case Nil    => Nil
      case h :: c => f(h) ::: c.flatMap(f)
    }

  override def tailRecM[A, B](a: A)(f: A => List[Either[A, B]]): List[B] = ???
}

listMonad.flatMap(List(1,2,3))(a => List(a + 1, a + 2))
listMonad.flatMap(List("a", "b", "c"))(c => List(c.toUpperCase, c.equals(22), c.concat("x")))
listMonad.flatMap(List(1,2,3,4,5,6))(c => List(c.abs, c.ceil, c.getClass, c.toBinaryString))
