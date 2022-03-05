package asiankoala.junk.v2.subsystems.hardware

import asiankoala.junk.v2.subsystems.motor.ServoSubsystem
import asiankoala.junk.v2.subsystems.motor.ServoSubsystemConfig
import asiankoala.junk.v2.util.Constants

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
