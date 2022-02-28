package robotuprising.ftc2021.v3.subsystems

import com.acmerobotics.dashboard.config.Config
import robotuprising.koawalib.hardware.motor.KMotorEx
import robotuprising.koawalib.hardware.motor.controllers.MotionProfileConfig
import robotuprising.koawalib.hardware.motor.controllers.PIDFConfig
import robotuprising.koawalib.hardware.sensor.KLimitSwitch
import robotuprising.koawalib.subsystem.DeviceSubsystem
import robotuprising.koawalib.subsystem.Subsystem

class Slides(private val motor: KMotorEx) : DeviceSubsystem() {
    @Config
    companion object SlideConstants {
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

        const val homeInches = 0.0
        const val allianceHighInches = 0.0
        const val allianceMidInches = 0.0
        const val allianceLowInches = 0.0
        const val sharedInches = 0.0
    }

    val isAtTarget get() = motor.isAtTarget()

    fun setSlideInches(inches: Double) {
        motor.setMotionProfileTarget(inches)
    }
}