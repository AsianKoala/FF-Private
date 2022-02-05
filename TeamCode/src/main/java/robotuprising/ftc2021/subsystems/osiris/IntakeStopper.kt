package robotuprising.ftc2021.subsystems.osiris

import robotuprising.ftc2021.subsystems.osiris.motor.ServoSubsystem
import robotuprising.ftc2021.subsystems.osiris.motor.ServoSubsystemConfig
import robotuprising.lib.opmode.OsirisDashboard

object IntakeStopper : ServoSubsystem(ServoSubsystemConfig("intakeStopper", 0.2)) {
    fun unlock() {
        moveServoToPosition(0.2)
    }

    fun lock() {
        moveServoToPosition(0.75)
    }
}