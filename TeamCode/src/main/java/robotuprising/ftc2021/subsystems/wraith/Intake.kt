package robotuprising.ftc2021.subsystems.wraith

import com.qualcomm.hardware.rev.RevColorSensorV3
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import robotuprising.ftc2021.hardware.wraith.WraithServo
import robotuprising.ftc2021.manager.BulkDataManager
import robotuprising.ftc2021.subsystems.wraith.motor.MotorControlType
import robotuprising.ftc2021.subsystems.wraith.motor.MotorSubsystem
import robotuprising.ftc2021.subsystems.wraith.motor.MotorSubsystemConfig
import robotuprising.ftc2021.util.*

object Intake : MotorSubsystem(
        MotorSubsystemConfig(
                "intake",

                MotorControlType.OPEN_LOOP
        )
) {
    private val rotateServo = WraithServo("intakeRotate")

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