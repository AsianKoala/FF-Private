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

        fun convertVectorPowersToWheels(powers: Pose): List<Double> {
            return listOf(
                    -powers.y - powers.x - powers.h.angle,
                    -powers.y + powers.x - powers.h.angle,
                    powers.y - powers.x - powers.h.angle,
                    powers.y + powers.x - powers.h.angle
            )
        }
    }

    private val frontLeft = NakiriMotor("FL", true).brake.openLoopControl
    private val frontRight = NakiriMotor("FR", true).brake.openLoopControl
    private val backLeft = NakiriMotor("BL", true).brake.openLoopControl
    private val backRight = NakiriMotor("BR", true).brake.openLoopControl
    private val motors = listOf(frontLeft, frontRight, backLeft, backRight)

    val wheelPositions: List<Double> get() = motors.map { ticksToInches(it.position) }

    var wheels: List<Double> = mutableListOf()

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
        wheels = mutableListOf()
    }
}
