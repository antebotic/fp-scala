object Solution {
  def solution(a: Int, b: Int, k: Int): Int =
    // write your code in Scala 2.12
    a % k match {
      case  0 => (a to b by k).length
      case  _ => ((k - (a % k) + a) to b by k).length
    }
}

Solution.solution(60000, 2000000000, 22)
Solution.solution(6, 11, 2)
