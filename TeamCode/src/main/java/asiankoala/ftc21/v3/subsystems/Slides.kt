package asiankoala.ftc21.v3.subsystems

import com.acmerobotics.dashboard.config.Config
import com.asiankoala.koawalib.control.MotionProfileConfig
import com.asiankoala.koawalib.hardware.motor.KMotorEx
import com.asiankoala.koawalib.subsystem.DeviceSubsystem

class Slides(private val motor: KMotorEx) : DeviceSubsystem() {
    @Config
    companion object SlideConstants {
        @JvmField val config = MotionProfileConfig()

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