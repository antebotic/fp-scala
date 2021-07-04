/*
Vector is an indexed immutable sequence similar to List, which is not indexed
 */

val nums = Vector(1, 2, 3, 4, 5)

val string = Vector("one", "two")

//append
val moreNums = nums :+ 6

val evenMoreNums = moreNums ++ Vector(7, 8, 9)

//prepend

val prepended = 0 +: evenMoreNums
val prependMore = Vector(-2, -1) ++: prepended

prependMore.map(_ * 2)
prependMore.toSeq
prependMore.reduceLeft(_ + _)
prependMore.reduce(_ - _)
prependMore.zipWithIndex
prependMore.foreach(_ * 2)
prependMore.andThen(_.self)

