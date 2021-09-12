def fib(n: Int): Int = {
  def loop(prev: Int, curr: Int, idx: Int): Int = {
    if(idx < n) loop(curr, prev+curr, idx + 1)
    else curr
  }

  loop(0, 1, 1)
}

fib(1)
fib(2)
fib(3)
fib(4)
fib(5)
fib(6)
fib(7)
fib(8)
