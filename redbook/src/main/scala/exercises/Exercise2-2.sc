// Implement isSorted which checks whether an Array[A] is sorted according to a
// given comparison function:

object Exercise2_2{
  def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean = {
    def go(len: Int, idx: Int, acc: Boolean): Boolean= {
      if(acc == false)      false
      else if(idx < len -1) go(len, idx+1, acc && ordered(as(idx), as(idx + 1)))
      else                  acc
    }

    go(as.length, 0, true)
  }
}

val arr1 = Array(1, 2, 3, 4, 5)
val arr2 = Array(5, 4, 3, 2, 1)
val arr3 = Array(1, 1, 1, 1, 1)
val arr4 = Array(1, 3, 2, 5, 4)

Exercise2_2.isSorted(arr1, (a1:Int, a2: Int) => a1 < a2)
Exercise2_2.isSorted(arr1, (a1:Int, a2: Int) => a1 > a2)
Exercise2_2.isSorted(arr1, (a1:Int, a2: Int) => a1 == a2)
Exercise2_2.isSorted(arr2, (a1:Int, a2: Int) => a1 < a2)
Exercise2_2.isSorted(arr2, (a1:Int, a2: Int) => a1 > a2)
Exercise2_2.isSorted(arr2, (a1:Int, a2: Int) => a1 == a2)
Exercise2_2.isSorted(arr3, (a1:Int, a2: Int) => a1 == a2)
Exercise2_2.isSorted(arr4, (a1:Int, a2: Int) => a1 > a2)
