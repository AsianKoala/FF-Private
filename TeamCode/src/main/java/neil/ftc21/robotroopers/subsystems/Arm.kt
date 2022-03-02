package neil.ftc21.robotroopers.subsystems

import neil.koawalib.hardware.motor.KMotorEx
import neil.koawalib.control.PIDFConfig
import neil.koawalib.control.feedforward.ArmFeedforward
import neil.koawalib.subsystem.DeviceSubsystem

class Arm(private val motor: KMotorEx) : DeviceSubsystem() {
    companion object ArmConstants {
        val config = PIDFConfig(
                0.015,
                0.0,
                0.00075,
                ArmFeedforward(kcos = 0.275)
        )
        const val depositAngle = 94.0
        const val restAngle = -55.0
        const val sharedAngle = 172.0
        const val sharedAngleAlliance = 178.0
        const val sharedAngleEnemy = 164.0
        const val middlePos = 134.0
        const val ticksPerDegree = 184.0 / 90
    }

    private var targetAngle = 0.0

    fun moveArmToDegree(degrees: Double) {
        motor.setPIDTarget(degrees)
    }
}