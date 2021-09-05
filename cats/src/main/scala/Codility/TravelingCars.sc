object Solution {
  def solution(a: Array[Int]) = {
    // write your code in Scala 2.12
    def go(zf: Array[Int], goingWest: Int, v: Vector[Int], position: Int, gwObserved: Int): Int = {
      if(position < zf.length){
        zf(position) match {
          case 0 => go(zf, goingWest, v :+ (goingWest-gwObserved), position + 1, gwObserved)
          case 1 => go(zf, goingWest, v :+ 0, position + 1, gwObserved + 1)
        }
      } else v.foldLeft(0)(_ + _)
    }


    val discardIllegal = a.dropWhile(_ == 1)
    go(discardIllegal, discardIllegal.foldLeft(0)(_ + _), Vector.empty, 0, 0) match {
      case n if n < 1000000000    => n
      case _                      => -1
    }
  }
}


val t = Array(0,1,0,1,1)
val t2 = Array(1,1,1,0,0,1,0,1,1)
val t3 = Array.emptyIntArray
val t4 = Array(1, 1)
val t5 = Array(0, 1)
val t6 = Array(1, 0)
val t7 = Array(0, 0)
val t8 = Array(1, 1, 1, 0, 0)
val t9 = Array(0, 0, 1)

Solution.solution(t)
Solution.solution(t2)
Solution.solution(t3)
Solution.solution(t4)
Solution.solution(t5)
Solution.solution(t6)
Solution.solution(t7)
Solution.solution(t8)
Solution.solution(t9)

