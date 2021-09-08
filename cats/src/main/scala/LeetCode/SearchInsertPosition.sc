object Solution {
  def searchInsert(nums: Array[Int], target: Int): Int = {
    def binSearch(position: Int, len: Int, lm: Int): Int = {
      val mid = position + (len - position)/2

      if(position > len) position
      else if(mid == lm) position
      else if(nums(mid) == target) mid
      else if(nums(mid) < target) binSearch(mid +1, len , mid)
      else binSearch(position, mid - 1, mid)
    }

    binSearch(0, nums.length -1, -1)
  }
}

val t = Array(3,5,6,7)
val t2 = Array(1,2,3,5,7)
val t3 = Array(1,3,4,5)
val t4 = Array(1)
val t5 = Array(1, 3)

Solution.searchInsert(t, 8)
Solution.searchInsert(t, 1)
Solution.searchInsert(t2, 7)
Solution.searchInsert(t3, 2)
Solution.searchInsert(t4, 2)
Solution.searchInsert(t5, 2)
