//Laws

//Reflexivity
// x <-> x

//Symetry
// E.eqv(x, y) <-> E.eqv(y, x)

import cats._
import cats.implicits._

case class Account(id: Long, number:String, balance:Double, owner: String)

object Account {
  implicit val universalEq: Eq[Account] = Eq.fromUniversalEquals

  object Instances {
    implicit def byIdEq(implicit eqLong: Eq[Long]): Eq[Account] =
      Eq.instance((a1, a2) => eqLong.eqv(a1.id, a2.id))

    implicit def byNumberEq(implicit eqString: Eq[String]): Eq[Account] =
      Eq.instance((a1, a2) => eqString.eqv(a1.number, a2.number))

    implicit def byBalanceEq(implicit eqDouble: Eq[Double]): Eq[Account] =
      Eq.instance((a1, a2) => eqDouble.eqv(a1.balance, a2.balance))

//    implicit def byEq[A](implicit eq: Eq[A]): Eq[Account] =
//     Eq.instance((a1, a2) => eq.eqv(a1, a2))

    implicit def byIdEq2(implicit eqLong: Eq[Long]): Eq[Account] = Eq.by(_.id)
    implicit def byNumberEq2(implicit eqString: Eq[String]): Eq[Account] = Eq.by(_.number)
  }
}


val account1 = Account(id = 22, number = "123-54", balance = 220, owner = "Marcus")
val account2 = Account(id = 34, number = "1123-22", balance = 220, owner = "Julius Caesar")

Eq[Account].eqv(account1, account2)
Account.Instances.byIdEq.eqv(account1, account2)
Account.Instances.byNumberEq.eqv(account1, account2)
Account.Instances.byBalanceEq.eqv(account1, account2)

account1 === account2

import Account.Instances.byBalanceEq
account1 === account2