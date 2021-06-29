import scala.util.Try

/*
1. Add apply and instance to byte decoder
2. Write an instance of ByteDecoder[String]
3. Use the instance to decode the following: Array(98, 105, 101, 110, 32, 58, 41)
*/

trait ByteDecoder[A]{
  def decode(bytes: Array[Byte]): Option[A]
}

object ByteDecoder{
  def apply[A](implicit ev: ByteDecoder[A]): ByteDecoder[A] = ev

  def instance[A](f: Array[Byte] => Option[A]): ByteDecoder[A] = new ByteDecoder[A] {
    override def decode(bytes: Array[Byte]): Option[A] = f(bytes)
  }
}

implicit object StringByteDecoder extends ByteDecoder[String]{
  override def decode(bytes: Array[Byte]): Option[String] =
    Try(new String(bytes)).toOption
}

val testArr: Array[Byte] = Array(98, 105, 101, 110, 32, 58, 41)

//using apply
ByteDecoder[String].decode(testArr)