import scala.collection.JavaConverters._

// you can write to stdout for debugging purposes, e.g.
// println("this is a debug message")

object Solution {
  def solution(x: Int, y: Int, d: Int): Int = {
    // write your code in Scala 2.12
    val jumps = (y - x).toDouble / d
    math.ceil(jumps).toInt
  }
}

Solution.solution(10, 85, 1)
