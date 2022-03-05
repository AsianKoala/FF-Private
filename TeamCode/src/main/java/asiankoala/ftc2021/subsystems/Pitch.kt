package asiankoala.ftc2021.subsystems

import com.acmerobotics.dashboard.config.Config
import com.asiankoala.koawalib.control.MotionProfileConfig
import com.asiankoala.koawalib.hardware.motor.KMotorEx
import com.asiankoala.koawalib.hardware.sensor.KLimitSwitch
import com.asiankoala.koawalib.subsystem.ZeroableDeviceSubsystem

class Pitch(motor: KMotorEx, limitSwitch: KLimitSwitch) : ZeroableDeviceSubsystem(motor, limitSwitch) {
    @Config
    companion object PitchConstants {
        @JvmField val config = MotionProfileConfig()
        const val zeroPosition = 0.0
        const val homeAngle = 0.0
        const val allianceHighAngle = 0.0
        const val allianceMidAngle = 0.0
        const val allianceLowAngle = 0.0
        const val sharedAngle = 0.0
    }

    val isAtTarget get() = motor.isAtTarget()

    fun setPitchAngle(angle: Double) {
        motor.setMotionProfileTarget(angle)
    }

    override val zeroPosition: Double
        get() = PitchConstants.zeroPosition
}
