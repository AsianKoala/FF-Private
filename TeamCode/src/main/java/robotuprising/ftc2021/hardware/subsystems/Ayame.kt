package robotuprising.ftc2021.hardware.subsystems

import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.lib.math.Pose
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem
import kotlin.math.PI
import kotlin.math.absoluteValue

class Ayame : Subsystem() {
    companion object {
        private const val WHEEL_RADIUS = 1.88976
        private const val GEAR_RATIO = 1.0
        private const val TICKS_PER_REV = 537.7

        private fun ticksToInches(ticks: Int): Double {
            return WHEEL_RADIUS * 2 * PI * GEAR_RATIO * ticks / TICKS_PER_REV
        }

        fun convertVectorPowersToWheels(powers: Pose): MutableList<Double> {
            return mutableListOf(
                    -powers.y - powers.x - powers.h.angle,
                    -powers.y + powers.x - powers.h.angle,
                    powers.y - powers.x - powers.h.angle,
                    powers.y + powers.x - powers.h.angle
            )
        }
    }

    private val fl = NakiriMotor("FL", true).brake.openLoopControl
    private val bl = NakiriMotor("BL", true).brake.openLoopControl
    private val fr = NakiriMotor("FR", true).brake.openLoopControl
    private val br = NakiriMotor("BR", true).brake.openLoopControl
    private val motors = listOf(fl, bl, fr, br)

//    val wheelPositions: List<Double> get() = motors.map { ticksToInches(it.position) }

    var wheels: List<Double> = mutableListOf(0.0, 0.0, 0.0, 0.0)

    override fun update() {
        val absMax = wheels.map { it.absoluteValue }.maxOrNull()!!

        if (absMax > 1.0) {
            motors.forEachIndexed { i, it -> it.power = wheels[i] / absMax }
        } else {
            motors.forEachIndexed { i, it -> it.power = wheels[i] }
        }
    }

    override fun sendDashboardPacket() {
        NakiriDashboard.name = "ayame"
        NakiriDashboard["wheel powers"] = wheels
    }

    override fun stop() {

    }
}
