package CatsRTJVM.Semigroup

import cats.Semigroup
import cats.instances.int._

object Semigroups {
  // Semigroups combine elements of the same type

  val naturalIntSemigroup = Semigroup[Int]
  val intCombination = naturalIntSemigroup.combine(2, 46)

  val naturalStringSemigroup = Semigroup[String]
  val stringCombination = naturalStringSemigroup.combine("Cats", "is awesome")

  def reduceInts(list: List[Int]): Int = list.reduce(naturalIntSemigroup.combine)
  def reduceStrings(list: List[String]): String = list.reduce(naturalStringSemigroup.combine)
  def reduceAnything[T](listT: List[T])(implicit s: Semigroup[T]): T =
    listT.reduce(s.combine)

  // Exercise: write an implicit Semigroup for a new type
  case class Expense(id: Long, amount: Double)
  implicit val expenseSemigroup: Semigroup[Expense] = new Semigroup[Expense]{
    override def combine(x: Expense, y: Expense): Expense =
      Expense(x.id + y.id, x.amount + y.amount)
  }

  object ExpenseImplicits {
    implicit def expenseSemigroup_v2: Semigroup[Expense] = Semigroup.instance[Expense] {
      (e1, e2) =>
        Expense(Math.max(e1.id, e2.id) * 2, Semigroup[Double].combine(e1.amount, e2.amount))
    }
  }

  // extension methods from Semigroup - |+|(combine)
  import cats.syntax.semigroup._

  val anIntSum = 2 |+| 3 // requires the presence of implicit Semigroup[Int]
  val aStringConcat = "we like" |+| "ice cream"
  val aCombinedExpense = Expense(4, 80) |+| Expense(2, 22)

  // Exercise: implement reduceThings_v2
  def reduceThings_v2[T](list: List[T])(implicit s: Semigroup[T]): T =
    list.reduce(_ |+| _)



  def main(args: Array[String]): Unit = {
    println(intCombination)
    println(stringCombination)

    val numbers = (1 to 10).toList
    println(reduceInts(numbers))

    val strings = List("This", "will", "be", "combined")
    println(reduceStrings(strings))
    println(reduceAnything(numbers))

    import cats.instances.option._

    val maybeNumbers = numbers.map(v => Option(v))
    println(reduceAnything(maybeNumbers))

    val maybeNumbers2 = List(Some(1), None, None, None, Some(3))
    println(reduceAnything(maybeNumbers2))

    val maybeStrings: List[Option[String]] = strings.map(s => Option(s))
    println(reduceAnything(maybeStrings))

    val expenses: List[Expense] = List(Expense(1, 20), Expense(2, 30), Expense(3, 50))
    println(reduceAnything(expenses))

    val isg = ExpenseImplicits.expenseSemigroup_v2
    val expenses2: List[Expense] = List(Expense(4, 210), Expense(21, 130), Expense(13, 520))
    println(reduceAnything(expenses2)(isg))

    println(reduceThings_v2((expenses)))
  }
}
