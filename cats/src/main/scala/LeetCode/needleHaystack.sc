object Solution {
  def strStr(haystack: String, needle: String): Int = {
    (haystack.length, needle.length) match {
      case (0, 0) => 0
      case (_, 0) => 0
      case (_, _) => haystack.indexOf(needle)
    }
  }
}

val h = "hello"
val n = "ll"

val h1 = ""
val n1 = ""

val h2 = "12345"
val n2 = ""

Solution.strStr(h, n)
Solution.strStr(h1, n1)
Solution.strStr(h2, n2)
