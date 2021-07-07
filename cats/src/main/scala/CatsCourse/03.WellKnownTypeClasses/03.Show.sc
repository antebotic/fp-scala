import cats._
import cats.implicits._

case class Account(id: Long, number:String, balance:Double, owner: String)

object Account{
  implicit val toStringShow: Show[Account] = Show.fromToString

  object Instances {
    implicit val byOwnerAndBalance: Show[Account] = Show.show { account =>
      s"${account.owner} - $$${account.balance}"
    }

    //Write an instance of show which will output 'This account belongs to owner'
    implicit val byOwnerBelonging: Show[Account]= Show.show { account =>
      s"This account belongs to ${account.owner}"
    }
  }
}

val MA = Account(id = 22, number = "123-54", balance = 220, owner = "Marcus Aurelius")
val JC = Account(id = 34, number = "1123-22", balance = 220, owner = "Julius Caesar")

Account.toStringShow.show(MA)
Account.toStringShow.show(JC)

Account.Instances.byOwnerAndBalance.show(MA)
Account.Instances.byOwnerBelonging.show(JC)

MA.show

import Account.Instances.byOwnerBelonging
MA.show
JC.show

