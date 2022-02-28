package robotuprising.ftc2021.v3.subsystems

import com.acmerobotics.dashboard.config.Config
import robotuprising.koawalib.hardware.motor.KMotorEx
import robotuprising.koawalib.hardware.motor.controllers.MotionProfileConfig
import robotuprising.koawalib.hardware.motor.controllers.PIDFConfig
import robotuprising.koawalib.hardware.sensor.KLimitSwitch
import robotuprising.koawalib.subsystem.ZeroableDeviceSubsystem

class Pitch(motor: KMotorEx, limitSwitch: KLimitSwitch) : ZeroableDeviceSubsystem(motor, limitSwitch) {
    @Config
    companion object PitchConstants {
        @JvmField val config = MotionProfileConfig(
                PIDFConfig(
                        kP = 0.0,
                        kI = 0.0,
                        kD = 0.0,
                        kStatic = 0.0,
                        positionEpsilon = 0.0,
                        ticksPerUnit = 0.0
                ),
                maxVelocity = 0.0,
                maxAcceleration = 0.0
        )
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