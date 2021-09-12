// Implement uncurry which reverses the transformation of curry.

def uncurry[A, B, C](f: A => B => C): (A, B) => C = {
  (a: A, b: B) => f(a)(b)
}

val func = (a: Int, b: Int) => a * b
val curriedF = func.curried
curriedF(3)(2)

val uncurriedF = uncurry[Int, Int, Int](curriedF)
uncurriedF(3, 2)
