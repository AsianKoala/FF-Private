package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.control.system.Subsystem
import org.firstinspires.ftc.teamcode.util.hw.DTPowers
import org.firstinspires.ftc.teamcode.util.math.Point
import org.openftc.revextensions2.ExpansionHubMotor
import kotlin.math.absoluteValue
import kotlin.math.max

class Homura : Subsystem() {
    private lateinit var motors: MutableList<ExpansionHubMotor>

    private val motorPowers = DoubleArray(4)
    private val prevMotorPowers = DoubleArray(4)

    private val minThresh = 0.01

    private var internalPowers: DTPowers = DTPowers()

    private var leftPower: Double = 0.0
    private var rightPower: Double = 0.0

    fun setPowers(powers: DTPowers) {
        internalPowers.fwd = powers.fwd
        internalPowers.turn = powers.turn
    }

    override fun init(hwMap: HardwareMap) {
        motors = mutableListOf(
            hwMap[ExpansionHubMotor::class.java, "FL"],
            hwMap[ExpansionHubMotor::class.java, "BL"],
            hwMap[ExpansionHubMotor::class.java, "FR"],
            hwMap[ExpansionHubMotor::class.java, "BR"]
        )
    }

    override fun update() {
        leftPower = internalPowers.fwd - internalPowers.turn
        rightPower = -internalPowers.fwd - internalPowers.turn
        val mx = max(leftPower.absoluteValue, rightPower.absoluteValue)
        if (mx > 1.0) {
            leftPower /= mx
            rightPower /= mx
        }

        motorPowers.forEachIndexed { i, d -> prevMotorPowers[i] = d }
        val difference = motorPowers.mapIndexed { i, d -> (d - prevMotorPowers[i]).absoluteValue }
        if (difference.maxOrNull()!! > minThresh) {
            motorPowers[0] = leftPower
            motorPowers[1] = leftPower
            motorPowers[2] = rightPower
            motorPowers[3] = rightPower
            setHWValues()
        }
    }

    override fun updateTelemetry(): HashMap<String, Any> {
        val r = HashMap<String, Any>()
        r["internal fwd"] = internalPowers.fwd
        r["internal turn"] = internalPowers.turn
        r["left"] = leftPower
        r["right"] = rightPower
        r["fl"] = motorPowers[0]
        r["bl"] = motorPowers[1]
        r["fr"] = motorPowers[2]
        r["br"] = motorPowers[3]
        return r
    }

    override fun stop() {
        setPowers(DTPowers(Point.ORIGIN))
    }

    override fun setHWValues() {
        motors.forEachIndexed { i, it -> it.power = motorPowers[i] }
    }
}
