object ClassParamTest {
    @JvmStatic
    fun main(args: Array<String>) {
        haha(ppfol)
    }

    fun haha(t: follower) {
        t.asd(2)
    }
}

abstract class follower {
    abstract fun asd(d: Int)
}

object ppfol : follower() {
    override fun asd(d: Int) {
        println("L")
    }
}
