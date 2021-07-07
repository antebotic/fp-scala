import cats._
import cats.implicits._

case class Account(id: Long, number:String, balance:Double, owner: String)

object Account {
  // implicit val orderById: Order[Account] = Order.from((a1, a2) => Order[Long].compare(a1.id, a2.id))
  implicit def orderById(implicit orderLong: Order[Long]): Order[Account] =
    Order.from((a1,a2) => orderLong.compare(a1.id, a2.id))

  object Instances{
    implicit def orderByNumber(implicit orderNumber: Order[String]): Order[Account] =
      Order.from((a1, a2) => orderNumber.compare(a1.number, a2.number))

    implicit def orderByBalance(orderDouble: Order[Double]): Order[Account] =
      Order.from((a1, a2) => orderDouble.compare(a1.balance, a2.balance))
  }
}

val account1 = Account(id = 22, number = "123-54", balance = 220, owner = "Marcus")
val account2 = Account(id = 34, number = "1123-22", balance = 220, owner = "Julius Caesar")


Order[Account].compare(account1, account2)
Account.Instances.orderByNumber.compare(account1, account2)

def sort[A](list: List[A])(implicit orderA: Order[A]): List[A] =
  list.sorted(orderA.toOrdering)


sort[Account](List(account1, account2))

import Account.Instances.orderByBalance
sort[Account](List(account1, account2))

import Account.Instances.orderByNumber
sort[Account](List(account1, account2))

account1 min account2
account1 max account2

