import cats._
import cats.implicits._

Foldable[List].fold(List("a", "b", "c"))
Foldable[List].foldMap(List(1,2,3,4,5))(_.toString)

Foldable[List].foldK(List(List(1, 2, 3), List(3, 2, 1)))

Foldable[List].reduceLeftToOption(List[Int]())(_.toString)((s, i) => s + i)

Foldable[List].reduceLeftToOption(List(2, 3, 1, 5))(_.toString)((s, i) => s + i)