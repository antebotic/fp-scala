import scala.annotation.tailrec

object Solution {
  def solution(n: Int, a: Array[Int]): Array[Int] = {
    // write your code in Scala 2.12
    @tailrec
    def go(pointerV: List[Int], position: Int): Array[Int] ={
      if(position < a.length) {
        a(position) match {
          case increaseOne if (1 to n) contains increaseOne   => go(pointerV.updated(increaseOne-1, pointerV(increaseOne-1) + 1), position + 1)
          case increaseAll if increaseAll == n + 1            => go(pointerV.map(_ => pointerV.max), position + 1)
        }
      } else pointerV.toArray
    }

    val pointers = (1 to n).toList.map(_ * 0)

    go(pointers, 0)
  }
}

val t = Array(3,4,4,6,1,4,4)
Solution.solution(5, t)
