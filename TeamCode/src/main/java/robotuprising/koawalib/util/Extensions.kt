package robotuprising.koawalib.util

import com.qualcomm.robotcore.util.Range
import kotlin.math.cos
import kotlin.math.sin

object Extensions {
    val Double.sin get() = sin(this)
    val Double.cos get() = cos(this)

    val Int.d get() = this.toDouble()
    val Float.d get() = this.toDouble()

    fun Double.clip(a: Double) = Range.clip(this, -a, a)
}