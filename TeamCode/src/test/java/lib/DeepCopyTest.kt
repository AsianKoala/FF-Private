package lib

import asiankoala.junk.v2.lib.util.Extensions.deepCopy

object DeepCopyTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val list = ArrayList<Int>()
        list.add(1)
        list.add(2)
        list.add(3)
        val dc = list.deepCopy()
        dc[0] = 100
        println(list)
        println(dc)
    }
}
