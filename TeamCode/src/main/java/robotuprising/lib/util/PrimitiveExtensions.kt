package robotuprising.lib.util

/**
 * cause kotlin doesnt auto type cast
 * cringe
 */
object PrimitiveExtensions {
    val Boolean.i get() = if (this) 1 else 0
    val Boolean.d get() = this.i.toDouble()
    val Int.d get() = this.toDouble()
    val Float.d get() = this.toDouble()
    val Long.d get() = this.toDouble()
    val Long.i get() = this.toInt()
    val Int.l get() = this.toLong()
}
