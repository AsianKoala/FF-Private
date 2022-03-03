package koawalib

import asiankoala.koawalib.math.Point

object PointCopyTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val p = Point(5,5)
        val a = p.copy()
    }
}
