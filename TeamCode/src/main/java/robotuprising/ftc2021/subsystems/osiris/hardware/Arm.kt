package robotuprising.ftc2021.subsystems.osiris.hardware

import robotuprising.ftc2021.subsystems.osiris.motor.ServoSubsystem
import robotuprising.ftc2021.subsystems.osiris.motor.ServoSubsystemConfig
import robotuprising.ftc2021.util.Constants

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