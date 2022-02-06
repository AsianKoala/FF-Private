package robotuprising.ftc2021.subsystems.osiris.hardware

import com.qualcomm.robotcore.hardware.DcMotorSimple
import robotuprising.ftc2021.hardware.osiris.OsirisServo
import robotuprising.ftc2021.subsystems.osiris.motor.MotorConfig
import robotuprising.ftc2021.subsystems.osiris.motor.MotorControlType
import robotuprising.ftc2021.subsystems.osiris.motor.MotorSubsystem
import robotuprising.ftc2021.subsystems.osiris.motor.MotorSubsystemConfig

object Intake : MotorSubsystem(
        MotorSubsystemConfig(
                MotorConfig(
                        "intake",
                        direction = DcMotorSimple.Direction.REVERSE
                ),

                MotorControlType.OPEN_LOOP
        )
) {
    fun turnOn() {
        output = 0.8
    }

    fun turnReverse() {
        output = -0.8
    }

    fun turnOff() {
        output = 0.0
    }
}