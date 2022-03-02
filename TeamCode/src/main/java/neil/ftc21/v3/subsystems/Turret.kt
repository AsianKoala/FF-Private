package neil.ftc21.v3.subsystems

import com.acmerobotics.dashboard.config.Config
import neil.koawalib.hardware.motor.KMotorEx
import neil.koawalib.control.MotionProfileConfig
import neil.koawalib.hardware.sensor.KLimitSwitch
import neil.koawalib.subsystem.ZeroableDeviceSubsystem

class Turret(motor: KMotorEx, limitSwitch: KLimitSwitch) : ZeroableDeviceSubsystem(motor, limitSwitch) {
    @Config
    companion object TurretConstants {
        @JvmField val config = MotionProfileConfig()
        const val zeroPosition = 0.0
        const val homeAngle = 0.0
        const val allianceAngle = 0.0
        const val sharedAngle = 0.0
    }

    val isAtTarget get() = motor.isAtTarget()

    fun setTurretAngle(angle: Double) {
        motor.setMotionProfileTarget(angle)
    }

    override val zeroPosition: Double
        get() = TurretConstants.zeroPosition
}