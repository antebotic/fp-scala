import java.nio.ByteBuffer

trait ByteEncoder[A]{
  def encode(a: A): Array[Byte]
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

implicit class OptByteEncoder[A](a: A){
  def encode(implicit enc: ByteEncoder[A]): Array[Byte] =
    enc.encode(a)
}

5.encode
"Marcus Aurelius".encode
//the above gets translated by the compiler to
new OptByteEncoder[String]("Marcus Aurelius").encode
//but it creates a new instance of the encoder each time it is used
//this uses the same instance
StringByteEncoder.encode("Marcus Aurelius")
//this can be avoided by declaring the implicit class like this

//implicit class OptByteEncoder[A](val a: A) extends AnyVal {
//  def encode(implicit enc: ByteEncoder[A]): Array[Byte] =
//    enc.encode(a)
//}

trait ByteDecoder[A]{
  def decode(bytes: Array[Byte]): Option[A]
}

implicit object IntByteDecoder extends ByteDecoder[Int]{
  override def decode(bytes: Array[Byte]): Option[Int] =
    if (bytes.length != 4)  None
    else {
      val bb = ByteBuffer.allocate(4)
      bb.put(bytes)
      bb.flip
      Some(bb.getInt)
    }
}

implicit class OptByteDeoder[A](bytes: Array[Byte]){
  def decode(implicit dec: ByteDecoder[A]): Option[A] =
    dec.decode(bytes)
}

Array[Byte](0, 0, 0, 5).decode
Array[Byte](0, 0, 0, 0, 5).decode