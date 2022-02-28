package robotuprising.ftc2021.v3.subsystems

import com.acmerobotics.dashboard.config.Config
import robotuprising.koawalib.hardware.KServo
import robotuprising.koawalib.subsystem.DeviceSubsystem

class Indexer(private val servo: KServo) : DeviceSubsystem() {
    @Config
    companion object IndexerConstants {
        const val HOME_POSITION = 0.0
        const val LOCK_POSITION = 0.0
        const val INDEX_POSITION = 0.0
    }

    fun setPosition(position: Double) {
        servo.position = position
    }
}