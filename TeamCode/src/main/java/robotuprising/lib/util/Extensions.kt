package robotuprising.lib.util

import kotlin.math.absoluteValue

/**
 * this is like my .vimrc but for kotlin lol
 */
object Extensions {
    val Boolean.i get() = if (this) 1 else 0
    val Boolean.d get() = this.i.toDouble()
    val Int.d get() = this.toDouble()
    val Float.d get() = this.toDouble()
    val Long.d get() = this.toDouble()
    val Long.i get() = this.toInt()
    val Int.l get() = this.toLong()

    val List<Double>.listAbs: List<Double> get() = map { it.absoluteValue }
    val ArrayList<*>.deepCopy: ArrayList<Any> get() = this.map { it } as ArrayList<Any>
}
