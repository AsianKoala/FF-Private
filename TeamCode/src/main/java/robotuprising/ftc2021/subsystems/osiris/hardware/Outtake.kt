package robotuprising.ftc2021.subsystems.osiris.hardware

import robotuprising.ftc2021.subsystems.osiris.motor.ServoSubsystem
import robotuprising.ftc2021.subsystems.osiris.motor.ServoSubsystemConfig
import robotuprising.ftc2021.util.Constants

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