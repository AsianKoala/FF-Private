package asiankoala.junk.v2.subsystems.hardware

import asiankoala.junk.v2.subsystems.motor.ServoSubsystem
import asiankoala.junk.v2.subsystems.motor.ServoSubsystemConfig
import asiankoala.junk.v2.util.Constants

object Outtake : ServoSubsystem(ServoSubsystemConfig("outtake", Constants.outtakeCockPosition)) {
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
