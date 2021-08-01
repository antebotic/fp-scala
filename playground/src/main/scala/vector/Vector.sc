/*
  - Vector is an indexed immutable sequence similar to List, which is not indexed
  - Use Vector when a general-purpose, immutable indexed sequence is needed
    - immutable: the elements in the collection cannot be changed, nor the collection resized
    - indexed: elements can be accessed quickly by their number
  - examples from:
      - https://alvinalexander.com/scala/vector-class-methods-syntax-examples/
 */

import cats.implicits

val nums = Vector(1, 2, 3)

final case class Person(name: String)
val people = Vector(
  Person("Emily"),
  Person("John"),
  Person("Hannah")
)

// explicitly state what vector contains

sealed trait Animal

case class Dog(name: String) extends Animal
case class Cat(name: String) extends Animal

val animalHouse: Vector[Animal] = Vector(
  Dog("Rover"),
  Cat("Felix")
)

// empty vector

val emptyInt  = Vector[Int]()
val emptyInt2 = Vector.empty[Int]

//create a new vector by populating it
1 to 5
1 until 5

1.to(5)
1.until(5)

(1 to 5).toVector
(1 until 5).toVector

(1 to 10 by 2).toVector
(1 until 10 by 2).toVector
(1 to 10).by(2).toVector

('d' to 'h').toVector //single quotes denote a char
('a' to 'f').by(2).toVector

Vector.range(1, 3)
Vector.range(1, 6, 2) // one to six, by two

Vector.fill(3)("foo")
Vector.tabulate(3)(n  => n * n)
Vector.tabulate(4)(n  => n * n)

/*adding to a vector, appending and prepending
      :+  append one item
      ++  append N items
      +:  prepend one item
      ++: prepend N items
*/

// Good thing to remember is that these methods are right associative, meaning the : sign
// will allways go to the side where the vector is
val v1 = Vector(4, 5, 6)
val v2 = v1 :+ 7
val v3 = v2 ++ Seq(8, 9)
val v4 = v2 ++ Vector(8, 9)
val v5 = 3 +: v3
val v6 = Seq(1, 2) ++: v5



/*
  Vector filtering methods
      distinct      - returns a new sequence with no duplicate elements
      drop(n)       - returns all elements after the first n elements
      dropRight(n) - returns all elements except the last n elements
      dropWhile(p) - drop the first sequence of elements that matches the predicate p
      filter(p)    - return all elements that match the predicate p
      filterNot(p) - return all elements that do not match the predicate p
      find(p)      - return the first element that matches the predicate p
      head         - return the first element, can throw if the Vector is empty
      headOption   - return the first element as an Option
      init         - all elements except the last one
      intersect(s) - Return the intersection of the vector and another sequence S
      last         - the last element, can throw if the vector is empty
      lastOption   - the last element as an option
      slice(f, u)  - a sequence of elements from index f(from) to index u(until)
      tail         - all elements after the first element
      take(n)      - the first n elements
      takeRight(n) - the last n elements
      takeWhile(p) - the first subset of elements that matches the predicate p

*/

val v = Vector(10, 20, 30, 40, 50, 10)

v.distinct
v.drop(2)
v.dropRight(2)
v.dropWhile(_ < 25)
v.filter(_ < 25)
v .filter(_ > 100)
v.filterNot(_ < 25)
v.find(_ > 20)
v.head
v.headOption
v.init
v.intersect(Seq(19, 20, 21))
v.last
v.lastOption
v.slice(2, 4)
v.tail
v.take(3)
v.takeRight(3)
v.takeWhile(_ < 40)


/*
  Vector "updating" methods
    collect(pf)   - a new collection by applying the partial function pf to all elements,
                      returning elements for which the function is defined. Also called filterMap
    distinct      - a new sequence with no duplicate items
    flatten       - transforms a list of lists into a single list (vectors?)
    flatMap(f)    - map a function and flatten afterwards
    map(f)        - return a new sequence by applying function f to each element in the vector
    updated(i, v) - returns a new vector with the element at index i replaced with the new value v
    concat(s)     - a new vector that contains elements from the current one and the sequence s
                      - previously called union
 */

val x: Vector[Option[Int]] = Vector(Some(1), None, Some(3), None)

x.collect { case Some(i) => i}
x.collect { case None => 0}
x.collect { case Some(i) => Vector(0 to i)}

x.distinct
x.map(i => i.map(_ * 2))
x.updated(0, 100).updated(2, 200)
Vector(x, x, x).flatten

val fruits = Vector("apples", "oranges")

fruits.map(_.toUpperCase)
fruits.flatMap(_.toUpperCase)

Vector(2, 4).concat(Vector(1, 3))
Vector(2, 3).concat(Vector(2, 4)).distinct.collect{ case i: Int => i + 22}

/*
  Transformer methods
    collect(pf)   - creates a new collection by applying the partial function pf to all vector elements,
                      returning those elements for which the function is defined
    diff(c)       - the difference between this vector and the collection c
    distinct      - a new sequence between this vector and the collection c
    flatten       - transforms a vector of vectors into a single vector
    flatMap(f)    - when working with sequences it works like map followed by flatten
    map(f)        - a new sequence by applying the function f to each element in the vector
    reverse       - a new sequence with the elements in reverse order
    sortWith(f)   - a new sequence with the elements sorted with the use of the function f
    union(c)      - a new sequence that contains all elements of the vector and the collection c
    zip(c)        - a collection of pair by matching the vector with the elements of the collection c
    zipWithIndex  - a vector of each element contained in a tuple along with its index
*/

val z: Vector[Option[Int]] = Vector(Some(1), None, None, Some(4), Some(5))

z.collect { case Some(i) => i}

val oneToFive     = (1 to 5).toVector
val threeToSeven  = (3 to 7).toVector

oneToFive.diff(threeToSeven)
threeToSeven.diff(oneToFive)

Vector(1, 2, 1, 2).distinct
Vector(1, 2, 3, 4).reverse

val scatteredNums = Vector(1, 5, 35, 4, 345, 23, 32)
scatteredNums.sorted
scatteredNums.sortWith(_ < _)
scatteredNums.sortWith(_ > _)

val women = Vector("Annie", "Christine", "Samantha")
val men   = Vector("Sam", "Marcus", "John")

women.zip(men)
men.zip(women)

women.zipWithIndex
men.zipWithIndex

women.zip(men).zipWithIndex

/*
  Informational and mathematical methods

    contains(e)             - true if the vector contains the element e
    containsSlice(s)        - true if the vector contains the sequence s
    count(p)                - the number of elements in the vector for which the predicate is true
    endsWith(s)             - true if the vector ends with the sequence s
    exists(p)               - true if the predicate returns true for at least one element in the vector
    find(p)                 - the first element that matches the predicate p, returned as an Option
    forall(p)               - true if the predicate p is true for all elements in the vector
    knownSize               - true if vector has a finite size, previously named hasDefiniteSize
    indexOf(e)              - the index of the first occurrence of the element e in the vector
    indexOf(e, i)           - the index of the first occurrence of the the element e in the vector starting from i
    indexOfSlice(s)         - the index of the first occurrence of the sequence s in the vector
    indexOfSlice(s, i)      - the index of the first occurrence of the sequence s in the vector starting from i
    indexWhere(p)           - the index of the first element where the predicate p returns true
    indexWhere(p, i)        - the index of the first element where the predicate p returns true starting from i
    isDefinedAt(i)          - true if the vector contains index i
    isEmpty                 - true if the vector contains no elements
    lastIndexOf(e)          - the index of the last occurrence of the element e in the vector
    lastIndexOf(e, i)       - the index of the last occurrence of the element e in the vector, before or at index i
    lastIndexOfSlice(s)     - the index of the last occurrence of the sequence s in the vector, before or at index i
    lastIndexOfSlice(s, i)  - the index of the last occurrence of the sequence s in the vector, before or at index i
    lastIndexWhere(p)       - the index of the last element where the predicate p returns true
    lastIndexWhere(p, i)    - the index of the last element where the predicat p returns true, before or at index i
    max                     - the largest element in the vector
    min                     - the smallest element in the vector
    nonEmpty                - returns true if the vector is not empty
    product                 - returns the result of multiplying the elements in the vector
    segmentLength           - the length of the longest segment for which the predicate p is true, starting at index 1
    size                    - the number of elements in the vector
    startsWith(s)           - true if the vector begins with the elements in the sequence s
    startsWith(s, i)        - true if the vector begins with the elements in the sequence s, starting at index i
    sum                     - sum of all elements in the vector
    fold(s)(o)              - folds the elements of the vector using the binary operator o, using an initial seed s
    foldLeft(s)(o)          - folds the elements left to right using the binary operator o, using an initial seed
    foldRight(s)(o)         - folds the elements right to left using the binary operator o, using an initial seed
    reduce(o)               - reduces the elements of the vector using the binary operator(o)
    reduceLeft(o)           - reduces the elements from left to right using the binary operator(o)
    reduceRight(o)          - reduces the elements from right to left using the binary operator(o)
 */

val evens         = Vector(2, 4, 6)
val odds          = Vector(1, 3, 5)
val fbb           = "foo bar biz"
val firstTen      = (1 to 10).toVector
val fiveToFifteen = (5 to 15).toVector
val empty         = Vector.empty[Int]
val letters       = ('a' to 'f').toVector

evens.contains(2)
firstTen.containsSlice(Seq(3, 4, 5))
firstTen.count( _ % 2 == 0)
firstTen.endsWith(Seq(7,8,9,10))
firstTen.exists(_ > 10)
firstTen.find(_ > 2)
firstTen.forall(_ < 20)
firstTen.knownSize

letters.indexOf('b')
letters.indexOf('d', 2)
letters.indexOf('d', 3)
letters.indexOf('d', 4)
letters.indexOfSlice(Seq('c', 'd'))
letters.indexOfSlice(Seq('c', 'd'), 2)
letters.indexOfSlice(Seq('c', 'd'), 3)

firstTen.indexWhere(_ == 3)
firstTen.indexWhere(_ == 3, 2)
firstTen.indexWhere(_ == 3, 5)

letters.isDefinedAt(1)
letters.isDefinedAt(20)
letters.isEmpty
empty.isEmpty

firstTen.max
letters.max
firstTen.min
letters.min
letters.nonEmpty
empty.nonEmpty
firstTen.product
firstTen.sum
letters.size

val k = Vector(1, 2, 9, 1, 1, 1, 1, 4)
k.segmentLength(_ < 4, 0)
k.segmentLength(_ < 4, 2)
k.segmentLength(_ < 4, 3)
k.segmentLength(_ < 4, 4)

firstTen.startsWith(Seq(1, 2))
firstTen.startsWith(Seq(1, 2), 0)
firstTen.startsWith(Seq(1, 2), 1)

firstTen.fold(100)(_ + _)
firstTen.foldLeft(100)(_ + _)
firstTen.foldRight(100)(_ + _)
firstTen.reduce(_ + _)
firstTen.reduce(_ - _)
firstTen.reduce(_ * _)


/*
  Grouping methods
    groupBy(f)    - a map of collections created by the function f
    grouped       - breaks the vector into fixed-size iterable collections
    partition(p)  - two collections created by the predicate p
    sliding(i, s) - group elements into fixed size blocks by passing a sliding window
                      of size i ands step s over them
    span(p)       - a collection of two collections
                      - first created by vector.takeWhile(p)
                      - second created by vector.dropWhile(p)
    splitAt(i)    - a collection of two collections by splitting the vector at index i
    unzip         - the opposite of zip, breaks a collection into two collection,
                      by dividing each element into two pieces
 */

val ft = (1 to 10).toVector

firstTen.groupBy(_ > 5)
firstTen.grouped(2)
firstTen.grouped(2).toVector
firstTen.grouped(5).toVector

"foo bar baz".partition(_ < 'c')
firstTen.partition(_ > 5)

firstTen.sliding(2)
firstTen.sliding(2).toVector
firstTen.sliding(2, 2).toVector
firstTen.sliding(2, 3).toVector
firstTen.sliding(2, 4).toVector

val j = Vector(15, 10, 5, 8, 20, 12)

j.groupBy(_ > 10)
j.partition(_ > 10)
j.span(_ < 20)
j.splitAt(2)

firstTen.sliding(2).toVector.map(_.reduce(_ + _))

// Vector with options

val optV = Vector(Some(1), None, None, Some(4), None, Some(6))

optV.flatten
optV.collect { case Some(i) => i.toDouble}

import scala.util.Try

def toInt(s: String): Option[Int] = Try(s.toInt).toOption
val strings = Vector("1", "2", "b", "4", "d")

strings.map(toInt)
strings.map(toInt).flatten
strings.flatMap(toInt)

