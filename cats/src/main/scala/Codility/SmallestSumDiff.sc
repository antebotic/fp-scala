object Solution {
  def solution(a: Array[Int]): Int = {
    // write your code in Scala 2.12
    def go(position: Int, preSum: Int, postSum: Int, result: Array[Int]): Int = {
      if(position < a.length){
        val current = a(position)
        val curPreSum = preSum + current
        val curPostSum = postSum - current
        val curDiff = scala.math.abs(curPreSum - curPostSum)
        go(position + 1, curPreSum, curPostSum, curDiff +: result)
      } else result.min
    }

    val first = a.head
    val sumRest = a.tail.sum

    go(1, first, sumRest, Array.emptyIntArray)
  }
}



val t = Array(3,1,2,4,3)
Solution.solution(t)
