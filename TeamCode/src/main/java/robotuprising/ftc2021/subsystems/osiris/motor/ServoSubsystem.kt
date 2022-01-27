package robotuprising.ftc2021.subsystems.osiris.motor

import com.acmerobotics.roadrunner.util.epsilonEquals
import robotuprising.ftc2021.hardware.osiris.OsirisServo
import robotuprising.ftc2021.subsystems.osiris.Subsystem
import robotuprising.lib.opmode.NakiriDashboard

open class ServoSubsystem(val config: ServoSubsystemConfig) : Subsystem() {
    private val servo = OsirisServo(config.name)

    fun moveServoToPosition(position: Double) {
        servo.position = position
    }

    override fun stop() {
//        servo.position = config.homePosition
    }

    override fun updateDashboard(debugging: Boolean) {
//        NakiriDashboard["${config.name} position"] = servo.position
    }
}