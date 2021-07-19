import cats._
import cats.implicits._
import cats.data._

5.valid[String]
5.valid[NonEmptyList[String]]
"error".invalid[Int]

// same as .valid[NonEmptyList[String]]
5.validNel[String]
"error".invalidNel[Int]


def concat[A](as: List[A], as2: List[A])(implicit M: Monoid[List[A]]): List[A] =
  M.combine(as, as2)

concat(List(1, 2, 3), List(4, 5,6))

//Nec is non empty Chain
5.validNec[String]
"error".invalidNec[Int]

6.validNec[String].ensure(NonEmptyChain("number is not even"))(_ % 2 == 0)
5.validNec[String].ensure(NonEmptyChain("number is not even"))(_ % 2 == 0)

Validated.cond(true, 5, "error")
Validated.cond(false, 5, "error")

Validated.condNec(true, 5, "error")
Validated.condNec(false, 5, "error")

5.validNec[String].getOrElse(10)
"5".validNec[Int].getOrElse(10)
"error".invalid[Int].getOrElse(10)

5.validNec[String].toEither
"error".invalidNec[Int].toEither
