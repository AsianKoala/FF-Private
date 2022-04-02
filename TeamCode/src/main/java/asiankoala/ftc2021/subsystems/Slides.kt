package asiankoala.ftc2021.subsystems

import com.asiankoala.koawalib.subsystem.old.MotorSubsystem
import com.asiankoala.koawalib.subsystem.old.MotorSubsystemConfig

class Slides(config: MotorSubsystemConfig) : MotorSubsystem(config) {
    companion object SlideConstants {
        const val slideHomeValue = 0.0
        const val slideDepositInches = 34.0
        const val slideSharedInches = 0.0
    }
}