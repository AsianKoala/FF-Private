package asiankoala.ftc2021.subsystems

import com.asiankoala.koawalib.hardware.servo.KServo
import com.asiankoala.koawalib.subsystem.DeviceSubsystem

class Indexer(private val servo: KServo) : DeviceSubsystem() {
    companion object IndexerConstants {
        const val indexerOpenPosition = 0.62
        const val indexerLockedPosition = 0.72
    }

    fun open() {
        servo.position = indexerOpenPosition
    }

    fun lock() {
        servo.position = indexerLockedPosition
    }
}