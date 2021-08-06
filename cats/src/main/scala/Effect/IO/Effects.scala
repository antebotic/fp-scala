package Effect.IO

object Effects {

  // substitution
  def combine(a: Int, b: Int): Int = a + b
  val five    = combine(2, 3)
  val fiveV2  = 2 + 3
  val fiveV3  = 5

  // referential transparency = replace an epression with its value without changing behaviour
  // side effects break referential transparency

  val printSomething: Unit = println("Cats Effect")
  //the act of printing returns a Unit type
  val printSomethingV2: Unit = ()
  // substitution is not working, both return the same Unit value, but program behaviour is changed
  var anInt = 0
  val changingVar: Unit = anInt += 1
  val vangingVarV2: Unit = ()
  // similarly, modifying a variable returns unit, but the program behaviour when substituting is lost


  // real world programs are however not necessarily pure.
  /* An effect is a data type defined with the following properties:
      - type signature describes the kind of computation that will be performed
      - type signature describes what kind of VALUE will be computed
      - when side effects are needed, construction of the effect is separate from the effect execution
  */

  /* example:
      Option is an effect type
        - describes a possibly absent value
        - type signature describes what is computed if it exists(Int)
        - side effects are not needed
  * */
  val anOption: Option[Int] = Option(42)

  /*
    example:
      Future is not an effect type
        - describes an asynchronous computation
        - computes a value of type A, if it's successful
        - side effect is required (allocating / scheduling a thread), but the thread is started
            immediately, effect execution is not separate from construction
   */
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global
  val aFuture: Future[Int] = Future(42)

  /*
    example:
      MyIO is an effect type
        - describes any sort of computation that might produce side effects
        - computes a value of type A, if it's successful
        - side effect creation is separate from the effect execution due
             to the need of calling unsafeRun to trigger execution
  */
  final case class MyIO[A](unsafeRun: () => A){
    def map[B](f: A=> B): MyIO[B] =
      MyIO(() => f(unsafeRun()))

    def flatMap[B](f: A => MyIO[B]): MyIO[B] =
      MyIO(() => f(unsafeRun()).unsafeRun())
  }

  val anIO: MyIO[Int] = MyIO(() => {
    println("I have printed this because unsafeRun was called")
    42
  })

  anIO.unsafeRun()

  def main(args: Array[String]): Unit = {}
}
