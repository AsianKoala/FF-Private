package neil.ftc21.v2.subsystems.osiris.hardware

import neil.ftc21.v2.subsystems.osiris.motor.ServoSubsystem
import neil.ftc21.v2.subsystems.osiris.motor.ServoSubsystemConfig
import neil.ftc21.v2.util.Constants

object Outtake: ServoSubsystem(ServoSubsystemConfig("outtake", Constants.outtakeCockPosition)) {
    fun home() {
        moveServoToPosition(Constants.outtakeHomePosition)
    }

    fun cock() {
        moveServoToPosition(Constants.outtakeCockPosition)
    }

    fun depositHigh() {
        moveServoToPosition(Constants.outtakeHighPosition)
    }

    fun depositShared() {
        moveServoToPosition(Constants.outtakeSharedPosition)
    }
}