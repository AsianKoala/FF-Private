package lib

import robotuprising.lib.util.AkemiList

object AkemiListTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val l = AkemiList<Int>()
        l.add(1)
        l.add(2)
        l.add(3)
    }
}
