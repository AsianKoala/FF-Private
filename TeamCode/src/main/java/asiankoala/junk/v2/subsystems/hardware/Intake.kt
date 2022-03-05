package asiankoala.junk.v2.subsystems.hardware

import asiankoala.junk.v2.subsystems.motor.MotorConfig
import asiankoala.junk.v2.subsystems.motor.MotorControlType
import asiankoala.junk.v2.subsystems.motor.MotorSubsystem
import asiankoala.junk.v2.subsystems.motor.MotorSubsystemConfig
import com.qualcomm.robotcore.hardware.DcMotorSimple

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
