import java.io.FileOutputStream

import scala.util.Using

trait ByteEncoder[A]{
  def encode(a: A): Array[Byte]
}

object ByteEncoder {
  implicit object StringByteEncoder extends ByteEncoder[String]{
    override def encode(s: String): Array[Byte] = s.getBytes
  }
}

trait Channel{
  def write[A](obj: A)(implicit enc: ByteEncoder[A]): Unit
}

object FileChannel extends Channel {
  override def write[A](obj: A)(implicit enc: ByteEncoder[A]): Unit = {
    val bytes = enc.encode(obj)

    Using(new FileOutputStream("cats/test")) { os =>
      os.write(bytes)
      os.flush()
    }
  }
}

object Rot3StringByteEncoder extends ByteEncoder[String] {
  override def encode(s: String): Array[Byte] =
    s.getBytes.map(c => (c + 3).toByte)
}

//this one is preffered by the compiler to the one in the companion object because it is in direct scope
implicit object Rot5StringByteEncoder extends ByteEncoder[String] {
  override def encode(s: String): Array[Byte] =
    s.getBytes.map(c => (c + 5).toByte)
}


FileChannel.write("Marcus Aurelius")
FileChannel.write("Marcus Aurelius")(Rot3StringByteEncoder)


//Overriding the behaviour of unavilable or unmodifiable code
//Solution: Define the companion object to the case class, and
// the compiler will look there first

case class Switch(isOn: Boolean)

object Switch {
  implicit object SwitchByteEncoder extends ByteEncoder[Switch]{
    override def encode(sw: Switch): Array[Byte] =
      if(sw.isOn) "1".getBytes
      else        "0".getBytes
  }

}

FileChannel.write(Switch(false))