package koawalib

object SupplierTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val t: () -> Double = { 0.0 }
    }

    abstract class foo {
        abstract fun s(): Double
    }

    lateinit var t: () -> Double
}
