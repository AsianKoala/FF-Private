package neil.lib.math

import com.acmerobotics.roadrunner.util.epsilonEquals
import kotlin.math.absoluteValue
import kotlin.math.sqrt

data class Vector3d(
        var x: Double = 0.0,
        var y: Double = 0.0,
        var z: Double = 0.0
) {
    operator fun plus(v: Vector3d) = Vector3d(x + v.x, y + v.y, z + v.z)
    operator fun minus(v: Vector3d) = this + -v
    operator fun times(n: Double) = Vector3d(x * n, y * n, z * n)
    operator fun div(n: Double) = times(1 / n)
    operator fun unaryMinus() = times(-1.0)

    infix fun equalTo(v: Vector3d) = x epsilonEquals v.x && y epsilonEquals v.y && z epsilonEquals v.z
    infix fun lessThan(v: Vector3d) = x < v.x && y < v.y && z < v.z
    infix fun greaterThan(v: Vector3d) = x > v.x && y > v.y && z > v.z

    fun epsilonEquals(epsilon: Double, v: Vector3d) = (x - v.x).absoluteValue < epsilon &&
            (y - v.y).absoluteValue < epsilon && (z - v.z).absoluteValue < epsilon

    val magnitude get() = sqrt(x * x + y * y + z * z)

    data class Vector4d(var v: Vector3d, var heading: Double)
}
