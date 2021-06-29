import java.io.FileOutputStream
import java.nio.ByteBuffer

import scala.util.Using

trait ByteEncoder[A] {
  def encode(a: A): Array[Byte]
}

trait Channel {
  def write[A](obj: A, enc: ByteEncoder[A]): Unit
}

object FileChannel extends Channel {
  override def write[A](obj: A, enc: ByteEncoder[A]): Unit = {
    val bytes: Array[Byte] = enc.encode(obj)

    Using(new FileOutputStream("cats/test")) { os =>
      os.write(bytes)
      os.flush()
    }
  }
}

object IntByteEncoder extends ByteEncoder[Int] {
  override def encode(n: Int): Array[Byte] = {
    val bb = ByteBuffer.allocate(4)
    bb.putInt(n)
    bb.array
  }
}

FileChannel.write[Int](5, IntByteEncoder)


//Practice: write an instance for string

object StringByteEncoder extends ByteEncoder[String]{
  override def encode(s: String): Array[Byte] = s.getBytes
}

FileChannel.write[String]("MarcusAurelius", StringByteEncoder)

/** Benefits of typeclasses:
 *  Can be instantiated by any type
 *  Cleaner interface
 *  Several implementations are possible for the same type
 * */
