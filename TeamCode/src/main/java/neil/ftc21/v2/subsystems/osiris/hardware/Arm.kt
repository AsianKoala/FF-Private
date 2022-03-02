package neil.ftc21.v2.subsystems.osiris.hardware

import neil.ftc21.v2.subsystems.osiris.motor.ServoSubsystem
import neil.ftc21.v2.subsystems.osiris.motor.ServoSubsystemConfig
import neil.ftc21.v2.util.Constants

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