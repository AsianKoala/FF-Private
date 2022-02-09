package robotuprising.koawalib.util

import kotlin.math.cos
import kotlin.math.sin

object Extensions {
    val Double.sin get() = sin(this)
    val Double.cos get() = cos(this)

    val Int.d get() = this.toDouble()
}