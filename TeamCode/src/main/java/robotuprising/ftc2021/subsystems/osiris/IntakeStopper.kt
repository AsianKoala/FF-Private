package robotuprising.ftc2021.subsystems.osiris

import robotuprising.ftc2021.subsystems.osiris.motor.ServoSubsystem
import robotuprising.ftc2021.subsystems.osiris.motor.ServoSubsystemConfig

object IntakeStopper : ServoSubsystem(ServoSubsystemConfig("intakeStopper", 0.75)) {
    fun letGo() {
        moveServoToPosition(0.5)
    }
}