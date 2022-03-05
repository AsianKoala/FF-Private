package asiankoala.junk.v2.subsystems.motor

import asiankoala.junk.v2.hardware.OsirisServo
import asiankoala.junk.v2.hardware.interfaces.Initializable
import asiankoala.junk.v2.lib.opmode.OsirisDashboard
import asiankoala.junk.v2.subsystems.Subsystem

abstract class ServoSubsystem(val config: ServoSubsystemConfig) : Subsystem(), Initializable {
    private val servo by lazy { OsirisServo(config.name) }

    fun moveServoToPosition(position: Double) {
        servo.position = position
    }

    override fun stop() {
        servo.position = -1.0
    }

    override fun updateDashboard(debugging: Boolean) {
        OsirisDashboard["${config.name} position"] = servo.position
    }

    override fun init() {
        servo.position = -1.0
    }
}
