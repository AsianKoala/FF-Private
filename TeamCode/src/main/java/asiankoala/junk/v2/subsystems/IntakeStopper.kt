package asiankoala.junk.v2.subsystems

import asiankoala.junk.v2.subsystems.motor.ServoSubsystem
import asiankoala.junk.v2.subsystems.motor.ServoSubsystemConfig

object IntakeStopper : ServoSubsystem(ServoSubsystemConfig("intakeStopper", 0.2)) {
    fun unlock() {
        moveServoToPosition(0.2)
    }

    fun lock() {
        moveServoToPosition(0.75)
    }
}
