package robotuprising.lib.util

/**
 * cause kotlin doesnt use auto type casting
 */
object Extensions {
    val Boolean.d get() = if (this) 1.0 else 0.0
    val Int.d get() = this.toDouble()
    val Float.d get() = this.toDouble()
    val Long.i get() = this.toInt()
    val Int.l get() = this.toLong()
}