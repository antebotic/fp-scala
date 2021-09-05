import scala.collection.immutable.HashMap

object Solution {
  def twoSum(nums: Array[Int], target: Int): Array[Int] = {
    def go(hashMapNums: HashMap[Int, Int], position: Int, solution: Array[Int]):Array[Int] = {
      if (solution.length == 0 && position < nums.length){
        val currentNum = nums(position)
        val r = target - currentNum

        hashMapNums.get(r) match {
          case Some(hashedPosition) => Array(position, hashedPosition)
          case None                 => go(hashMapNums + (currentNum -> position), position + 1, Array.emptyIntArray)
        }
      } else solution

    }

    go(HashMap[Int, Int](), 0, Array.emptyIntArray)
  }
}


val t = Array(-5,-3, 2, 1, 4, -7, -2, -1, 5)
Solution.twoSum(t, 4)

