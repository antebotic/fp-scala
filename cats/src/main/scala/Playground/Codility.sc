object Solution {
  def solution(n: Int): Int= {
    // write your code in Scala 2.12
    def go(waitForOne: Boolean, bin: List[Char], acc: Int, pos: Int, binaryGaps: List[Int]): List[Int] = {
      val allBinaryGaps =
        if (pos < bin.length) {
          bin(pos) match {
            case '0' if !waitForOne => go(false, bin, acc + 1, pos + 1, binaryGaps)
            case '1' if waitForOne  => go(false, bin, acc, pos + 1, binaryGaps)
            case '1' if !waitForOne => {
              if (acc == 0) go(false, bin, 0, pos + 1, binaryGaps)
              else          go(false, bin, 0, pos + 1, acc +: binaryGaps)
            }
          }
        }
        else binaryGaps

      allBinaryGaps
    }

    val binary = n.toBinaryString.map(identity).toList
    val result = go(true, binary, 0, 0, List.empty)

    result.length match {
      case 0 => 0
      case _ => result.max
    }
  }
}


Solution.solution(1610612737)








