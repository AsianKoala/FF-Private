package robotuprising.lib.control.motion

data class PIDCoeffs(val kp: Double, val ki: Double, val kd: Double) {

    override fun toString(): String {
        return "kp: $kp, ki: $ki, kd: $kd"
    }
}
