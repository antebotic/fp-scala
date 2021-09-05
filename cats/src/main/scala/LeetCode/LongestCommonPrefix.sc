object Solution {
  def longestCommonPrefix(strs: Array[String]): String = {
    def go(firstStrChars: Array[Array[Char]], position: Int, longestStreak: Int, shortest: Int): Int = {
      if(position < shortest) {
        val charsAtPosition = firstStrChars.map(lc => lc(position))
        if(charsAtPosition.forall(_ == charsAtPosition.head)) {
          go(firstStrChars, position + 1, longestStreak + 1, shortest)
        } else longestStreak
      } else longestStreak

    }

    val charVec = strs.map(_.toCharArray)
    val shortestString = strs.map(_.length).min
    go(charVec, 0, 0, shortestString) match {
      case 0 => ""
      case x: Int => strs(0).substring(0, x)
    }
  }
}

val t  = Array("flower","flow","flowhe")
val t2 = Array("dog","racecar","car")

Solution.longestCommonPrefix(t)
Solution.longestCommonPrefix(t2)


// Find the shortest string
// Reduce all other strings to match that string length

