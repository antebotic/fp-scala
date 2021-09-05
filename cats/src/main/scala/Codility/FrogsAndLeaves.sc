
object Solution {
  def solution(x: Int, a: Array[Int]): Int= {
    // write your code in Scala 2.12
    def goFrog(position: Int, leaves: Vector[Int]): Vector[Int] = {
      if(position < a.length){
        goFrog(position + 1, leaves :+  a(position))
      } else leaves.distinct.filter(_ <= x)
    }

    val leavesInOrder = goFrog(0, Vector.empty)

    if(leavesInOrder.diff(1 to x) == Vector.empty) a.indexOf(leavesInOrder.last)
    else                                         -1
  }
}


val t = (0 to 1000000).toArray
val r = Solution.solution(87643, t)

