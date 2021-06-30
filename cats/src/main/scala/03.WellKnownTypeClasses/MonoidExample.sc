import cats._
import cats.implicits._

final case class Speed(metersPerSecond: Double){
  def kilometersPerSec: Double = metersPerSecond / 1000
  def milesPerSec: Double = metersPerSecond / 1609.34
}

object Speed{
  def addSpeeds(s1: Speed, s2: Speed): Speed =
    Speed(s1.metersPerSecond + s2.metersPerSecond)

  implicit val monoidSpeed: Monoid[Speed] = Monoid.instance(Speed(0), addSpeeds)
  implicit val monoidSpeedEq: Eq[Speed] = Eq.fromUniversalEquals

  object Instances {
    implicit val monoidSpeed2: Monoid[Speed] = new Monoid[Speed] {
      override def empty: Speed = Speed(0)

      override def combine(x: Speed, y: Speed): Speed =
        addSpeeds(x, y)
    }
  }
}

Monoid[Speed].combine(Speed(123), Speed(321))
Monoid[Speed].combine(Speed(222), Monoid[Speed].empty)
Monoid[Speed].empty

Speed(1000) |+| Speed(3000) //combine symbol

Monoid[Speed].combineAll(List(Speed(200), Speed(2123), Speed(1323)))
List(Speed(200), Speed(2123), Speed(1323)).combineAll

Monoid[Speed].isEmpty(Speed(100))
Monoid[Speed].isEmpty(Monoid[Speed].empty)

//Exercises
// 1. define sumMonoid[Int], define combine
// 2. define minMonoid[Int], define combine via the min operation
// 3. define listMonoid[A]: Monoid[List[A]] = ???
// 4. define stringMonoid: Monoid[String]

object Exercise {
  // 1.
  val sumMonoid: Monoid[Int] = Monoid.instance(0, _ + _)

  // 2.
  val minMonoid: Monoid[Int] = Monoid.instance(Int.MaxValue, _ min _)

  // 3.
  def listMonoid[A]: Monoid[List[A]] = Monoid.instance(Nil, _ ++ _)

  // 4.
  val stringMonoid: Monoid[String] = Monoid.instance("", _ + _)
}

Exercise.sumMonoid.combine(2, 5)
Exercise.minMonoid.combine(2, 5)
Exercise.listMonoid.combine(List(1, 2, 3), List(11, 12, 13))
Exercise.stringMonoid.combine("Marcus", " ").combine("Aurelius")

//Monoid methods

Monoid[String].empty
Monoid[String].isEmpty("Marcus Aurelius")
Monoid[String].isEmpty("")
Monoid[String].combineN("ha", 3)
Monoid[String].combineN("Marcus", 3).combineN(2)
Monoid[String].combineAll(List("Marcvs", " ", "Aurelivs", " ")).combineN(3)