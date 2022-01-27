package robotuprising.ftc2021.subsystems.osiris.hardware

import robotuprising.ftc2021.hardware.osiris.OsirisServo
import robotuprising.ftc2021.subsystems.osiris.motor.MotorConfig
import robotuprising.ftc2021.subsystems.osiris.motor.MotorControlType
import robotuprising.ftc2021.subsystems.osiris.motor.MotorSubsystem
import robotuprising.ftc2021.subsystems.osiris.motor.MotorSubsystemConfig

object Intake : MotorSubsystem(
        MotorSubsystemConfig(
                MotorConfig("intake"),

                MotorControlType.OPEN_LOOP
        )
) {
    private val rotateServo = OsirisServo("intakeRotate")

    fun turnOn() {
        motor.power = 1.0
    }

    fun turnReverse() {
        motor.power = -1.0
    }

    fun turnOff() {
        motor.power = 0.0
    }

    fun rotate(position: Double) {
        rotateServo.position = position
    }
}