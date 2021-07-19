package Lectures

import java.util.concurrent.Executors


object IntroConcurency extends App{
  val runnable = new Runnable {
    override def run(): Unit = println("Running in parallel")
  }

  val aThread = new Thread(runnable)

  //creates a new jvm thread on top of the OS thread
  aThread.start()
  //blocks until aThread finishes running
  aThread.join()

  val threadHello = new Thread(() => (1 to 15).foreach(_ => println("Hello")))
  val threadGoodbye = new Thread(() => (1 to 15).foreach(_ => println("Goodbye")))

  //separate runs produce different results
  threadHello.start()
  threadGoodbye.start()

  val pool = Executors.newFixedThreadPool(10)

  pool.execute(() => println("Hello from the thread pool"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("Slept for 1 s")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("Sleep moar")
    Thread.sleep(500)
    println("Slept for 1.5s")
  })

  pool.shutdown()     // does not kill sleeping threads
  //pool.shutdownNow()  // kills all threads in the pool immediately


  //race condition
  def runInParallel = {
    var x = 0

    val thread1 = new Thread(() => x = 1)
    val thread2 = new Thread(() => x =2)

    thread1.start()
    thread2.start()

    println(x)
  }

  for(_ <- 1 to 100) runInParallel


  class BankAccount(var amount: Int) {
    override def toString: String = "" + amount
  }

  def buy(account: BankAccount, thing: String, price: Int) = {
    account.amount -= price
    println(s"I've bought ${thing}, my account now is ${account} ")
  }

  for (_ <- 1  to 1000) {
    val account = new BankAccount(amount = 50000)
    val thread1 = new Thread(() => buy(account, "shoes", price = 3000))
    val thread2 = new Thread(() => buy(account, "iPhone12", price = 4000))

    thread1.start()
    thread2.start()

    Thread.sleep(10)
    if(account.amount != 43000) println()
  }
}
