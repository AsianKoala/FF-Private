package robotuprising.lib.control.motion

import robotuprising.lib.util.PrimitiveExtensions.d

data class PIDFCoeffs(val kp: Double, val ki: Double, val kd: Double, val kv: Double = 0.d, val ka: Double = 0.d, val ks: Double = 0.d) {

    override fun toString(): String {
        return "kp: $kp, ki: $ki, kd: $kd, kv: $kv, ka: $ka, ks: $ks"
    }
}
