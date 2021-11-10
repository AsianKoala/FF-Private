package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.opmode.AkemiDashboard
import robotuprising.lib.system.Subsystem
import robotuprising.lib.util.Extensions.d
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
    }

    private val frontLeft = NakiriMotor("FL", true)
    private val frontRight = NakiriMotor("FR", true)
    private val backLeft = NakiriMotor("BL", true)
    private val backRight = NakiriMotor("BR", true)
    private val motors = listOf(frontLeft, frontRight, backLeft, backRight)

    val wheelPositions: List<Double> get() = motors.map { ticksToInches(it.position) }

    var powers = Pose(Point.ORIGIN, Angle(0.d, AngleUnit.RAW))
    var rrUpdated = false
    var rrPowers = mutableListOf(0.d, 0.d, 0.d, 0.d)

    override fun update() {
        val wheels = if (rrUpdated) {
            rrUpdated = false
            rrPowers
        } else {
            val frontLeftPower = powers.y + powers.x + powers.h.angle
            val frontRightPower = powers.y - powers.x - powers.h.angle
            val backLeftPower = powers.y - powers.x + powers.h.angle
            val backRightPower = powers.y + powers.x - powers.h.angle
            listOf(frontLeftPower, frontRightPower, backLeftPower, backRightPower)
        }

        val absMax = wheels.map { it.absoluteValue }.maxOrNull()!!

        if (absMax > 1.0) {
            motors.forEachIndexed { i, it -> it.power = wheels[i] / absMax }
        } else {
            motors.forEachIndexed { i, it -> it.power = wheels[i] }
        }
    }

    override fun sendDashboardPacket() {
        AkemiDashboard["powers"] = powers
    }

    override fun stop() {
        powers = Pose.DEFAULT
    }

    init {
        frontRight.direction = DcMotorSimple.Direction.REVERSE
        backRight.direction = DcMotorSimple.Direction.REVERSE
    }
}
