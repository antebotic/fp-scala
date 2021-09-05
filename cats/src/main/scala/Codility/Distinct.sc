import scala.collection.JavaConverters._

// you can write to stdout for debugging purposes, e.g.
// println("this is a debug message")

import scala.collection.immutable.HashMap

object Solution {
  def solution(a: Array[Int]): Int = {
    // write your code in Scala 2.12
    def go(hm: HashMap[Int, Int], position: Int): Int ={
      if(position < a.length) {
        val current = a(position)

        hm.get(current) match {
          case Some(n) => go(hm + (current -> (n + 1)), position + 1)
          case None    => go(hm + (current -> 1), position + 1)
        }
      } else hm.find(_._2 % 2 != 0).map(kv => kv._1 ).getOrElse(0)
    }

    go(HashMap[Int, Int](), 0)
  }
}

val t = Array(9, 3, 9, 3, 9, 7, 9)
val t2 = Array(2, 2, 3, 3, 4)
t.length
//Solution.solution(t)
Solution.solution(t2)

