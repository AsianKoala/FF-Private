package robotuprising.ftc2021.v2.subsystems.osiris

import robotuprising.ftc2021.v2.subsystems.osiris.motor.ServoSubsystem
import robotuprising.ftc2021.v2.subsystems.osiris.motor.ServoSubsystemConfig

object IntakeStopper : ServoSubsystem(ServoSubsystemConfig("intakeStopper", 0.2)) {
    fun unlock() {
        moveServoToPosition(0.2)
    }

    fun lock() {
        moveServoToPosition(0.75)
    }
}