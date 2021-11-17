package lib

import java.nio.ByteBuffer

object ByteTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val dv = 711
        val ba = ByteBuffer.allocate(4).putInt(dv).array()

        for (a in ba)
            println(a)
    }
}
