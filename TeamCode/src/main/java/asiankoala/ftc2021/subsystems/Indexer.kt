package asiankoala.ftc2021.subsystems

import com.acmerobotics.dashboard.config.Config
import com.asiankoala.koawalib.hardware.KServo
import com.asiankoala.koawalib.subsystem.DeviceSubsystem

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
