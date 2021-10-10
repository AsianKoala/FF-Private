object MapTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val l = mutableListOf(1, 2, 3)
        l.forEachIndexed { i, x -> l[i] = x * 2 }
        l.forEach { println(it) }
    }
}
