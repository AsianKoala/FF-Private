package ftc2021
object PairTest {

    enum class s {
        a, b
    }

    @JvmStatic
    fun main(args: Array<String>) {
        // inside, outside
        val x = s.a
        val positions = when (x) {
            s.a -> 3 to 4
            s.b -> 5 to 6
        }
        println(positions)
    }
}
