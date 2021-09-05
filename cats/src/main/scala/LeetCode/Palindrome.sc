object Solution {
  def isPalindrome(x: Int): Boolean = {
    if(sign(x) == -1) false
    else reverseInput(x) == x
  }

  def reverseInput(x: Int): Int =
    x.abs.toString.reverse.toIntOption match {
      case None => 0
      case Some(n) if Int.MinValue to Int.MaxValue contains n => n
    }

  def sign(x: Int): Int =
    if  (x < 0 )  -1
    else           1
}

Solution.isPalindrome(Int.MinValue)
