

val anOption: Option[Int] = Some(1)
val maybeString: Option[String] = None

// Int, String, Boolean => *

// Option, List. Vector, Set => * *

val listString: List[Int] = List(1, 2, 3, 4)

listString.map(n => n * 2).map(println)
println(listString.map(n => n * 3))

// * * *
val stringOpt: List[Option[String]] = List( Some("one"), Some("two"), Some("three"), Some("four"), None)
stringOpt.map(maybeString => maybeString.map(s => s.toUpperCase))

stringOpt.flatMap(maybeString =>
  maybeString.map(string =>
    string.toUpperCase
  )
)

for {
  maybeString   <- stringOpt
  string        <- maybeString
} yield string

val doubleOption = Option(Some(Some(1)))

sealed trait MyOption

final case class MySome(value: String) extends MyOption
case object MyNone extends MyOption

val opt: MyOption = MySome("pero")

opt match {
  case MySome(kupus) => println(kupus)
  case MyNone => println("Ode nema nista")
}

val opt2 = Option("two".toUpperCase)

opt2 match {
  case Some(value) => value.toLowerCase
  case _           =>
}

val komplikacija = (opt, opt2) match {
  case (MySome(v1), Some(v2)) => println(v1 + v2)
  case _                      => println("123")
}

// referential transparency

val print   = println("Nesto")
val print2  = ()

import cats._
import cats.implicits._

val aMap = Map("foo" -> Map("bar" -> 5))
val anotherMap = Map("foo" -> Map("bar" -> 6))
val combinedMap = Semigroup[Map[String, Map[String, Int]]].combine(aMap, anotherMap)

Monoid[String].empty
Monoid[String].combineAll(List("1", "2", "3"))
Monoid[Map[String, Int]].combineAll(List(Map("a" -> 1, "b" -> 2), Map("a" -> 3)))


val l = List(1, 2, 3, 4, 5)
l.foldMap(identity)
