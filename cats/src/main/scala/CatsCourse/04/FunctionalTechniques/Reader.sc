import cats._
import cats.data._


val signReader: Reader[Int, String] = Reader(n => if(n > 0) "positive" else if(n < 0) "negative" else "zero")
signReader.run(5)
signReader.run(-22)
signReader.run(0)

//Implement parityReader

val parityReader: Reader[Int, String] = Reader(n => if(n %2 == 0) "even" else "odd")
parityReader.run(4)
parityReader.run(3)

val descriptionReader: Reader[Int, String] =
  for{
    sign <- signReader
    par  <- parityReader
  } yield s"$sign and $par"

descriptionReader.run(5)
descriptionReader.run(-22)

val addOneReader: Reader[Int, Int] =
  for {
    env <- Reader((x: Int) => x)
  } yield env + 1

addOneReader.run(4)

case class Person(id: Long, name: String)
case class Account(id: Long, ownerId: Long)

trait AccountRepository{
  val accountRepository: Service

  trait Service{
    def findAccountById(id: Long): Account
  }
}

trait LiveAccountRepository extends AccountRepository{
  override val accountRepository: Service = new Service {
    override def findAccountById(id: Long): Account = Account(id, 2)
  }
}

trait PersonRepository{
   val personRepository: Service

  trait Service {
    def findPersonById(id: Long): Person
  }
}

trait LivePersonRepository extends PersonRepository{
  override val personRepository: Service = new Service {
    override def findPersonById(id: Long): Person = Person(id, "longoen" )
  }

}

def findNextAccount(id: Long): Reader[AccountRepository, Account] =
  for {
    accountModule <- Reader(identity[AccountRepository])
    account            = accountModule.accountRepository.findAccountById(id + 1)
  } yield account

def findOwnerNameByAccountId(id: Long): Reader[PersonRepository with AccountRepository, String]=
  for {
    accountModule <- Reader(identity[AccountRepository])
    personModule  <- Reader(identity[PersonRepository])
    account = accountModule.accountRepository.findAccountById(id)
    owner = personModule.personRepository.findPersonById(account.ownerId)
  } yield owner

type Env = PersonRepository with AccountRepository
val liveEnv: Env = new LivePersonRepository with LiveAccountRepository

findOwnerNameByAccountId(1).run(liveEnv)


