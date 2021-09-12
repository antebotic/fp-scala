// Implement dropWhile, which removes elements from the List prefix as long
// as they match a predicate

def dropWhile[A](la: List[A], f: A => Boolean): List[A] =
  if(la.isEmpty) List.empty
  else if (f(la.head)) dropWhile(la.tail, f)
  else la



val el = List.empty[Int]
val nel = List( -1, -2 , -3, 0 , 1 ,2 ,3 )

dropWhile(el, (a: Int) => a > 0)
el.dropWhile(_ > 0)

dropWhile(nel, (a: Int) => a < 0)
nel.dropWhile(_ < 0)
