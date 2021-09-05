import scala.annotation.tailrec

object Solution {
  def solution(a: Array[Int]): Int = {
    // write your code in Scala 2.12
    @tailrec
    def go(min: Int, v: Vector[Int]): Int ={
      if(min < 0) go(min+1, v)
      else if(a.exists(_ == min + 1)) go(min+1, v)
      else min + 1
    }

    go(a.min, a.toVector)
  }
}

//val t = (-10000 to 10000).toArray.filter(_ != 8765)
val t2 = Array(21)

Solution.solution(t2)
