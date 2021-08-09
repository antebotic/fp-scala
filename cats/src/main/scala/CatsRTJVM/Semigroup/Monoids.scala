package CatsRTJVM.Semigroup


import cats.implicits.catsSyntaxOptionId
import cats.{Monoid, Semigroup}
import cats.instances.int._
import cats.syntax.semigroup._

object Monoids {

  val numbers = (1 to 1000).toList
  // |+| is always associative
  val sumLeft   = numbers.foldLeft(0)(_ |+| _)
  val sumRight  = numbers.foldRight(0)(_ |+| _)

  // define a general API
  def combineFold[T](list: List[T])(implicit semigroup: Monoid[T]): T =
    list.foldLeft(Monoid[T].empty)(_ |+| _)

  // monoid extends semigroup adding the empty method
  def combineFold_v2[T](list: List[T])(implicit monoid: Monoid[T]): T =
    list.foldLeft(monoid.empty)(_ |+| _)

  // Monoids
  val intMonoid = Monoid[Int]
  val combineNumbers = intMonoid.combine(1, 2)
  val combineNumbers2 = intMonoid.combine(1, intMonoid.empty)

  import cats.instances.string._
  val emptyString = Monoid[String].empty // ""
  val combineString = Monoid[String].combine("Hello", emptyString)

  import cats.instances.option._ //construct an implicit Monoid[Option[Int]]
  val emptyOption: Option[Int] = Monoid[Option[Int]].empty //None
  val combineOption: Option[Int] = Monoid[Option[Int]].combine(Some(3), None)

  // extension methods for Monoids - |+| same as for semigroup
  import cats.syntax.monoid._

  //Exercise: combine a list of phonebooks as Maps[String, Int]
  val phoneBooks = List(
    Map(
      "Alice" -> 235,
      "Bob" -> 646
    ),
    Map(
      "Charlie" -> 342,
      "Daniel"  -> 223
    ),
    Map(
      "Tina" -> 355
    )
  )

  import cats.instances.map._
  val reducedPhoneBook = phoneBooks.reduce(_ |+| _)

  // Exercise 3
  final case class ShoppingCart(items: List[String], total: Double)

  implicit val shoppingCartMonoid = new Monoid[ShoppingCart] {
    override def empty: ShoppingCart = ShoppingCart(List.empty, 0.0)

    override def combine(x: ShoppingCart, y: ShoppingCart): ShoppingCart =
      ShoppingCart(x.items ++ y.items, x.total + y.total)
  }

  def checkout(shoppingCarts: List[ShoppingCart]): ShoppingCart =
    shoppingCarts.reduce(_ |+| _)

  def main(args: Array[String]): Unit = {
    println(sumLeft == sumRight)
    println(combineOption)
    println(reducedPhoneBook)

    val shoppingCarts = List(
      ShoppingCart(List("Milk", "Bread"), 22.3),
      ShoppingCart(List("Pants", "Shirt"), 340.1),
      ShoppingCart(List("Guitar", "Amplifier"), 1234.4)
    )

    println(checkout(shoppingCarts))
  }
}
