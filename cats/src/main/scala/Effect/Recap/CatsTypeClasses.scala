package Effect.Recap

/*
  - applicative
  - functor
  - flatMap
  - monad
  - apply
  - applicativeError/monadError
  - traverse
*/

object CatsTypeClasses {

  trait MyFunctor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  import cats.Functor
  import cats.instances.list._

  val listFunctor = Functor[List]
  def increment[F[_]](container: F[Int])(implicit f: Functor[F]): F[Int] =
    f.map(container)(_ + 1)


  import cats.syntax.functor._
  def incrementV2[F[_]: Functor](container: F[Int]): F[Int] =
    container.map(_ + 1)


  // Applicative, ability to "wrap" types
  trait MyApplicative[F[_]] extends Functor[F]{
    def pure[A](value: A): F[A]
  }

  import cats.Applicative
  val applicativeList = Applicative[List]
  val aSimpleList: List[Int] = applicativeList.pure(43)

  import cats.syntax.applicative._
  val aSimpleListV2: List[Int] = (43).pure[List]



  // FlatMap - ability to chain multiple computations
  trait MyFlatMap[F[_]] extends MyFunctor[F]{
    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  }

  import cats.FlatMap
  val flatMapList = FlatMap[List]

  import cats.syntax.flatMap._
  def crossProduct[F[_]: FlatMap, A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    fa.flatMap(a => fb.map(b => (a, b)))


  // Monad - applicattive + flatMap
  trait MyMonad[F[_]] extends MyApplicative[F] with MyFlatMap[F]{
    override def map[A, B](fa: F[A])(f: A => B): F[B] =
      flatMap(fa)(a => pure(f(a)))
  }

  import cats.Monad
  val monadList: Monad[List] = Monad[List]

  def crossProductV2[F[_]: Monad, A, B](fa: F[A], fb: F[B]): F[(A, B)] = {
    fa.flatMap(a => fb.map(b => (a, b)))
    fb.flatMap(b => fa.map(a => (a, b)))
    // or

    for {
      a <- fa
      b <- fb
    } yield (a, b)
  }


  //ApplicativeError, Monad Error
  trait MyApplicativeError[F[_], E] extends MyApplicative[F] {
    def raiseError[A](e: E): F[A]
  }

  import cats.ApplicativeError
  type ErrorOr[A] = Either[String, A]

  val applicativeErrorEither = ApplicativeError[ErrorOr, String]
  val desireableValue: ErrorOr[Int] = applicativeErrorEither.pure(42)
  val failedValue: ErrorOr[Int] = applicativeErrorEither.raiseError("Something failed")

  import cats.syntax.applicativeError._
  val failedValueV2: ErrorOr[Int] = "Something failed".raiseError[ErrorOr, Int]


  trait MyMonadError[F[_], E] extends MyApplicativeError[F, E] with Monad[F]
  import cats.MonadError

  val monadErrorEither = MonadError[ErrorOr, String]


  // Traverse
  trait MyTraverse[F[_]] extends MyFunctor[F] {
    def traverse[G[_], A, B](fa: F[A])(f: A => G[B]): G[F[B]]
  }

  // Turn nested wrappers inside out
  val listOfOptions: List[Option[Int]] = List(Some(1), Some(2), Some(3))

  import cats.Traverse
  val listTraverse = Traverse[List]
  val OptionList: Option[List[Int]] = listTraverse.traverse(List(1, 2, 3))( x => Option(x))

  import cats.syntax.traverse._
  val optionListV2: Option[List[Int]] = List(1,2,3).traverse(x => Option(x))

  def main(args: Array[String]): Unit = {

  }
}
