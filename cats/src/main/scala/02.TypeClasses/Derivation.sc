import java.nio.ByteBuffer

trait ByteEncoder[A]{
  def encode(a: A): Array[Byte]
}

object ByteEncoder{
  def apply[A](implicit be: ByteEncoder[A]): ByteEncoder[A] = be
}

implicit object StringByteEncoder extends ByteEncoder[String]{
  override def encode(s: String): Array[Byte] =
    s.getBytes
}

implicit object IntByteEncoder extends ByteEncoder[Int]{
  override def encode(n: Int): Array[Byte] = {
    val bb = ByteBuffer.allocate(4)
    bb.putInt(n)
    bb.array
  }
}

//implicit object OptionStringByteEncoder extends ByteEncoder[Option[String]]{
//  override def encode(o: Option[String]): Array[Byte] = o match {
//    case Some(s) => ByteEncoder[String].encode(s)
//    case None    => Array[Byte]()
//  }
//}
//
//implicit object OptionIntByteEncoder extends ByteEncoder[Option[Int]]{
//  override def encode(o: Option[Int]): Array[Byte] = o match {
//    case Some(n) => ByteEncoder[Int].encode(n)
//    case None    => Array[Byte]()
//    }
//}

// The optionEncoder below encapsulates behaviour of the two explicit encoders above!
implicit def optionEncoder[A](implicit encV: ByteEncoder[A]): ByteEncoder[Option[A]] = new ByteEncoder[Option[A]] {
  override def encode(o: Option[A]): Array[Byte] = o match {
    case Some(value)  => encV.encode(value)
    case None         => Array[Byte]()
  }
}


ByteEncoder[String].encode("Marcus Aurelius")
ByteEncoder[Int].encode(8472)
ByteEncoder[Option[String]].encode(Option("Marcus Aurelius"))
ByteEncoder[Option[String]].encode(None)
ByteEncoder[Option[Int]].encode(Some(8472))
ByteEncoder[Option[Int]].encode(None)