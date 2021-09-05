import scala.util.Random

object Solution {
  def solution(a: Array[Int]): Int = {
    // write your code in Scala 2.12
    val byTwo   = a.sliding(2, 1).map(_.reduce(_ + _)/2.toDouble).toVector
    val byThree = a.sliding(3, 1).map(_.reduce(_ + _ )/3.toDouble).toVector


    if(byTwo.min < byThree.min)       byTwo.indexOf(byTwo.min)
    else if(byTwo.min == byThree.min) byTwo.indexOf(byTwo.min) min byThree.indexOf(byThree.min)
    else byThree.indexOf(byThree.min)
  }
}


val randT = (2 to 100000).map(_ => Random.between(-10000, 10000)).toArray

val t2 = Array(4,2,2,5,1,5,8)
val t3 = Array(-2,2,-2, 1, 1, -5)

Solution.solution(randT)
Solution.solution(t2)
Solution.solution(t3)
