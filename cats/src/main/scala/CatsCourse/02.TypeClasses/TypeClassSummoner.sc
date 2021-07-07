trait ByteEncoder[A]{
  def encode(a: A): Array[Byte]
}

object ByteEncoder {
  implicit object StringByteEncoder extends ByteEncoder[String]{
    override def encode(s: String): Array[Byte] =
      s.getBytes
  }

  def apply[A](implicit ev: ByteEncoder[A]): ByteEncoder[A] = ev
}

ByteEncoder.StringByteEncoder.encode("hello")

implicit object Rot3StringByteEncoder extends ByteEncoder[String]{
  override def encode(s: String): Array[Byte] =
    s.getBytes.map(b => (b +3).toByte)
}

//implicitly
implicitly[ByteEncoder[String]].encode("hello")
ByteEncoder.StringByteEncoder.encode("hello")
ByteEncoder.apply[String].encode("hello")

//apply is special, so the above can be reduced to:
ByteEncoder[String].encode("hello")

