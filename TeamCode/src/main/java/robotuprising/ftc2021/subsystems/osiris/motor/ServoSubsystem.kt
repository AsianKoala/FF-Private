package robotuprising.ftc2021.subsystems.osiris.motor

import robotuprising.ftc2021.hardware.osiris.OsirisServo
import robotuprising.ftc2021.hardware.osiris.interfaces.Initializable
import robotuprising.ftc2021.subsystems.osiris.Subsystem

abstract class ServoSubsystem(val config: ServoSubsystemConfig) : Subsystem(), Initializable {
    private val servo by lazy { OsirisServo(config.name) }

    fun moveServoToPosition(position: Double) {
        servo.position = position
    }

    override fun stop() {
        servo.position = -1.0
    }

    override fun updateDashboard(debugging: Boolean) {
//        NakiriDashboard["${config.name} position"] = servo.position
    }

    override fun init() {
        servo.position = config.homePosition
    }
}