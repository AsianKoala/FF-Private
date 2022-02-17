package robotuprising.ftc2021.v2.subsystems.osiris.motor

import robotuprising.ftc2021.v2.hardware.osiris.OsirisServo
import robotuprising.ftc2021.v2.hardware.osiris.interfaces.Initializable
import robotuprising.ftc2021.v2.subsystems.osiris.Subsystem
import robotuprising.lib.opmode.OsirisDashboard

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