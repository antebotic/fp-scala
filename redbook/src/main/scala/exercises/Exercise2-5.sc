// Implement a HOF that composes two functions

def compose[A, B, C](f: B => C, g: A => B): A => C =
  g andThen f

def compose2[A, B, C](f: B => C, g: A => B): A => C =
  f compose g

import cats.implicits.toFunctorOps

def compose3[A, B, C](f: B => C, g: A => B): A => C =
  g.map(f)
