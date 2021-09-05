/*
  Given a signed 32-bit integer x, return x with its digits reversed.
  If reversing x causes the value to go outside the signed 32-bit integer range [-2e31, 2e31 - 1], then return 0.
 */

object Solution {
  def reverse(x: Int): Int =
   x.abs.toString.reverse.toIntOption match {
      case None => 0
      case Some(n) if Int.MinValue to Int.MaxValue contains n => n * sign(x)
    }

  def sign(x: Int): Int =
    if  (x < 0 )  -1
    else           1
}

Solution.reverse(-32595234)
