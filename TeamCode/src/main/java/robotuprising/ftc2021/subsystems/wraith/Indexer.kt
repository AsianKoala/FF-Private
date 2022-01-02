package robotuprising.ftc2021.subsystems.wraith

import robotuprising.ftc2021.hardware.wraith.WraithServo
import robotuprising.ftc2021.subsystems.wraith.motor.ServoSubsystem
import robotuprising.ftc2021.subsystems.wraith.motor.ServoSubsystemConfig
import robotuprising.ftc2021.util.Constants

object Indexer : ServoSubsystem(
        ServoSubsystemConfig("indexer", Constants.indexerOpenPosition)) {

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