// Convert function f of two arguments into a function of one argument that
// partially applies f

def curry[A, B, C](f: (A, B) => C): A => (B => C) =
  a => f(a, _)

val ff = (a: Int, b:Int) => a + b

val curried = curry(ff)
val c5 = curried(5)
c5(2)
c5(3)
c5(5)
c5(-3)
