package asiankoala.junk.v2.subsystems.hardware

import asiankoala.junk.v2.subsystems.motor.ServoSubsystem
import asiankoala.junk.v2.subsystems.motor.ServoSubsystemConfig
import asiankoala.junk.v2.util.Constants

object Arm : ServoSubsystem(ServoSubsystemConfig("arm", Constants.armHomePosition)) {
    fun home() {
        moveServoToPosition(Constants.armHomePosition)
    }

    fun depositHigh() {
        moveServoToPosition(Constants.armHighPosition)
    }

    fun depositShared() {
        moveServoToPosition(Constants.armSharedPosition)
    }
}
