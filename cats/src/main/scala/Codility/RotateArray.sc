
object Solution {
  def solution(a: Array[Int], k: Int): Array[Int] = {
    // write your code in Scala 2.12
    val arrLen = a.length

    val result =
      if(arrLen == k) a
      else {
        val sr = a.splitAt(a.length - k)
        Array(sr._2, sr._1).flatten
      }

    result
  }
}

val t = Array(1,2,3,4,5,6,7)

val maxN = (0 to 100).toArray
Solution.solution(maxN, 100)
Solution.solution(t, 7)
