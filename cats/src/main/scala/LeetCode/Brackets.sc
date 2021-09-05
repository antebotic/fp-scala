
object Solution {
  def isValid(s: String): Boolean =
    s.length % 2 match {
      case 0 if s.length != 0 => solve(s.map(identity).toList, List.empty)
      case _ => false
    }

  val bracketPairs: Map[Char, Char] = Map(
    '(' -> ')',
    '[' -> ']',
    '{' -> '}'
  )

  private def solve(lc: List[Char], stack: List[Char]): Boolean = {
    lc match {
      case hd :: tl if hd == '(' | hd == '[' | hd =='{' => solve(tl,  hd +: stack)
      case hd :: tl if hd == ')' | hd == ']' | hd =='}' => stack.headOption match {
        case None     => false
        case Some(c)  =>
          if (bracketPairs(c) == hd && lc.tail == Nil && stack.tail == Nil) true
          else if(bracketPairs(c) == hd )                                   solve(tl, stack.tail)
          else                                                              false
      }
      case _                                            => false
    }
  }

}

val t = "()[]{}"
val t2 = "([)]"
val t3 = "{[]}"
val t4 = ""
val t5 = "{"
val t6 = "}"
val t7 = "{{{{}}}}[[[[]]]](((())))"
val t8 = "(("
val t9 = "(){}}{"

Solution.isValid(t)
Solution.isValid(t2)
Solution.isValid(t3)
Solution.isValid(t4)
Solution.isValid(t5)
Solution.isValid(t6)
Solution.isValid(t7)
Solution.isValid(t8)
Solution.isValid(t9)
