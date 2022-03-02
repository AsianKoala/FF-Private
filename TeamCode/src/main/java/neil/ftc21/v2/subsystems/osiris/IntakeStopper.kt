package neil.ftc21.v2.subsystems.osiris

import neil.ftc21.v2.subsystems.osiris.motor.ServoSubsystem
import neil.ftc21.v2.subsystems.osiris.motor.ServoSubsystemConfig

object IntakeStopper : ServoSubsystem(ServoSubsystemConfig("intakeStopper", 0.2)) {
    fun unlock() {
        moveServoToPosition(0.2)
    }

    fun lock() {
        moveServoToPosition(0.75)
    }
}