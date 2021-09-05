package Lectures.Implicits

object OrganizingImplicits extends App {
  println(List(1,4,5,3,2).sorted)

  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  println(List(1,4,5,3,2).sorted)

  //How and where does the compiler look for implicits, and

  /*
  Implicits(used as implicit parameters, in order of importance)
   - val/var
   - object
   - accessor methods = defs with no parentheses
   */

  //Exercise:
  case class Person(name: String, age: Int)
  object Person {
    implicit val byAgeOrdering: Ordering[Person] = new Ordering[Person] {
      override def compare(x: Person, y: Person): Int = x.age - y.age
    }
  }

  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )

  def personOrderingByName: Ordering[Person] = new Ordering[Person] {
    override def compare(x: Person, y: Person): Int = x.name.compareTo(y.name)
  }

  println(persons.sorted(personOrderingByName))

  /*
  Implicit scope:
    - normal scope    = LOCAL SCOPE
    - imported scope
    - companion objects of all types involved in the type signature
      - List
      - Ordering
      - all the types involved = A or any supertype
   */

  val personsV = Vector(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )

  object InvisibleImplicitsDueToScopeInHere {
    //this should target the companion object implicit ordering, throws
//    val personsL = List(
//      Person("Steve", 30),
//      Person("Amy", 22),
//      Person("John", 66)
//    )
//    personsL.sorted
//




    implicit val vectorInverseOrderingPerson: Ordering[Person] = new Ordering[Person] {
      override def compare(x: Person, y: Person): Int = y.name.compareTo(x.name)
    }
  }

  import InvisibleImplicitsDueToScopeInHere._
  println(personsV.sorted)

  //Best practices
  // If possible define the implicits in the companion object of that type

  /*
  Exercise, create orderings by
    - totalPrice = most used (50%)
    - by unit count = 25%
    - by unit price = 25%

   */
}


object Exercise {
  /*
  Exercise, create orderings by
    - totalPrice = most used (50%)
    - by unit count = 25%
    - by unit price = 25%

 */
  case class Purchase (nUnits: Int, unitPrice: Double)

  implicit val byUnitPriceOrdering: Ordering[Purchase] = new Ordering[Purchase] {
    override def compare(x: Purchase, y: Purchase): Int = y.unitPrice.toInt - y.unitPrice.toInt
  }

  implicit def implicitUnitOrdering: Ordering[Purchase] = new Ordering[Purchase] {
    override def compare(x: Purchase, y: Purchase): Int = y.nUnits - x.nUnits
  }

}



sealed trait PleasureQuotient
final case class Low(yumLevel: String)    extends PleasureQuotient
final case class Medium(yumLevel: String) extends PleasureQuotient
final case class High(yumLevel: String)   extends PleasureQuotient

//algebraic data type
// sum: case class is OR ====>  PleasureQuotient = Low || Yum || High
// product: parameters of a case class, are manadatory for every class
// -> case class FootballCourt(branku: String, grass: String, lights: String)


sealed trait Cookie

final case class ChocholateChip(levelOfCrunh: Int, chocolatyGoodines: PleasureQuotient) extends Cookie
final case class StrawberyDelight(levelOfCrunch: Int, strawberyGoodines: PleasureQuotient) extends Cookie

object Example extends App {
  val pleasureQ = High("very very yummy")
  val cc: Cookie = ChocholateChip(7, pleasureQ)

  val r = cc match {
    case ChocholateChip(_, _)   => "Its chochlaty"
    case StrawberyDelight(_, _) => "Its strawbery"
    case _                      => "I have no idea but it tastes good"
  }

  println(r)

}
