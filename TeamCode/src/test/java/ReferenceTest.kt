import org.firstinspires.ftc.teamcode.util.math.Point

object ReferenceTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val a = ArrayList<Point>()
        for (i in 0..4) {
            a.add(Point.ORIGIN)
        }
        def(a)
        a.forEach { println(it) }
    }

    fun def(arr: ArrayList<Point>) {
        arr.forEach { it.x = 5.0 }
    }
}
