sealed trait RomanNumeral{
  def value: Int =
   this match {
     case RomanNumeral.I => 1
     case RomanNumeral.V => 5
     case RomanNumeral.X => 10
     case RomanNumeral.L => 50
     case RomanNumeral.C => 100
     case RomanNumeral.D => 500
     case RomanNumeral.M => 1000
   }
}


object RomanNumeral{
  def parse(c: Char): RomanNumeral= {
    c match{
      case 'I' => RomanNumeral.I
      case 'V' => RomanNumeral.V
      case 'X' => RomanNumeral.X
      case 'L' => RomanNumeral.L
      case 'C' => RomanNumeral.C
      case 'D' => RomanNumeral.D
      case 'M' => RomanNumeral.M
    }
  }

  def evalRN(combo: List[RomanNumeral]): Int = {
    combo match {
      case I :: V :: rest => 4   + evalRN(rest)
      case I :: X :: rest => 9   + evalRN(rest)
      case X :: L :: rest => 40  + evalRN(rest)
      case X :: C :: rest => 90 + evalRN(rest)
      case C :: D :: rest => 400 + evalRN(rest)
      case C :: M :: rest => 900 + evalRN(rest)
      case f :: rest      => f.value + evalRN(rest)
      case Nil            => 0
    }
  }

  case object I extends RomanNumeral
  case object V extends RomanNumeral
  case object X extends RomanNumeral
  case object L extends RomanNumeral
  case object C extends RomanNumeral
  case object D extends RomanNumeral
  case object M extends RomanNumeral
}

object Solution {
  def romanToInt(s: String) = {
   val l =  s.map(RomanNumeral.parse).toList
    RomanNumeral.evalRN(l)
  }
}

Solution.romanToInt("MCMXCIV")
