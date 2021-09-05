package Lectures.Implicits

object ImplicitsIntro extends App{
  val pair    = "Some" -> "555"
  val intPair = 1 -> 2

  case class Person(name: String) {
    def greet(name: String) = s"Hi my name is $name"
  }

  implicit def fromStringToPerson(str: String): Person = Person(str)

  // If the method is not found on the string type, the compiler will look to see if it can
  // find something with a greet method, and if there is an implicit conversion from string:

  println("Peter".greet _) //compiler rewriters println(fromStringToPerson("Peter").greet)

  //If more than one combination of greet/implicit str -> something, there will be a compile error
  //  class A {
  //    def greet: Int = 2
  //  }
  //
  //  implicit def fromStringToA(str: String): A = new A

  //implicit paramters
  def increment(x: Int)(implicit amount: Int) = x + amount
  implicit val defaultAmount = 10

  val r = increment(2)
  println(r)
}
