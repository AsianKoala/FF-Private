package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.control.system.Subsystem
import org.firstinspires.ftc.teamcode.util.hw.DTPowers
import org.openftc.revextensions2.ExpansionHubMotor
import kotlin.math.absoluteValue
import kotlin.math.max

class Homura : Subsystem() {
    private val motorPowers = DoubleArray(4)

    private var internalPowers: DTPowers = DTPowers()

    private var leftPower: Double = 0.0
    private var rightPower: Double = 0.0

    fun setPowers(powers: DTPowers) {
        internalPowers.fwd = powers.fwd
        internalPowers.turn = powers.turn
    }

    fun setPowers(fwd: Double, turn: Double) {
        setPowers(DTPowers(fwd, turn))
    }

    override fun init(vararg motors: ExpansionHubMotor) {
        for (m in motors) {
            m.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            m.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }
    }

    override fun update() {
        leftPower = internalPowers.fwd - internalPowers.turn
        rightPower = -internalPowers.fwd - internalPowers.turn
        val mx = max(leftPower.absoluteValue, rightPower.absoluteValue)
        if (mx > 1.0) {
            leftPower /= mx
            rightPower /= mx
        }
        motorPowers[0] = leftPower
        motorPowers[1] = leftPower
        motorPowers[2] = rightPower
        motorPowers[3] = rightPower
    }

    override fun updateTelemetry(): HashMap<String, Any> {
        val r = HashMap<String, Any>()
        r["internal fwd"] = internalPowers.fwd
        r["internal turn"] = internalPowers.turn
        r["left diffy"] = leftPower
        r["right diffy"] = rightPower
        r["fl"] = motorPowers[0]
        r["bl"] = motorPowers[1]
        r["fr"] = motorPowers[2]
        r["br"] = motorPowers[3]
        return r
    }

    override fun stop() {
        setPowers(0.0, 0.0)
    }
}
