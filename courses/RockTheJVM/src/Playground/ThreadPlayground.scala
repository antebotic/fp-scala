package Playground

import java.util.concurrent.Executors

object ThreadPlayground extends App {

  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("Just something in the thread pool"))
  pool.execute(() => {
    Thread.sleep(1000)
    println("What a nice 1s nap")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("Gonna sleep some more")
    Thread.sleep(1000)
    println("What a nice 2s nap")
  })

  pool.shutdown()
}
