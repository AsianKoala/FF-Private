package neil.ftc21.v2.subsystems.osiris.hardware

import neil.ftc21.v2.subsystems.osiris.motor.ServoSubsystem
import neil.ftc21.v2.subsystems.osiris.motor.ServoSubsystemConfig
import neil.ftc21.v2.util.Constants

object Indexer : ServoSubsystem(ServoSubsystemConfig("indexer", Constants.indexerOpenPosition)) {
    fun open() {
        moveServoToPosition(Constants.indexerOpenPosition)
    }

    fun lock() {
        moveServoToPosition(Constants.indexerLockedPosition)
    }

    fun index() {
        moveServoToPosition(Constants.indexerIndexingPosition)
    }
}