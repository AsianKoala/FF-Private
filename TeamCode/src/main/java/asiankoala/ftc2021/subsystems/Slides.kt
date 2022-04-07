package asiankoala.ftc2021.subsystems

import com.asiankoala.koawalib.subsystem.old.MotorSubsystem
import com.asiankoala.koawalib.subsystem.old.MotorSubsystemConfig

class Slides(config: MotorSubsystemConfig) : MotorSubsystem(config) {
    companion object SlideConstants {
        const val slideHomeValue = -0.5
        const val depositHighInches = 33.5
        const val sharedInches = 7.5
        const val sharedExtInches = 10.5
    }
}