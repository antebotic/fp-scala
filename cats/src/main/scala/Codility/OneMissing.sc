
object Solution {
  def solution(a: Array[Int]):Int = {
    // write your code in Scala 2.12
    (a.min to a.max).diff(a).head
  }
}

val ar = Array(2, 3,1, 5)
Solution.solution(ar)
