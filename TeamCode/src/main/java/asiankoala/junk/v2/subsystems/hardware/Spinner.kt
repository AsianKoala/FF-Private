package asiankoala.junk.v2.subsystems.hardware

import asiankoala.junk.v2.subsystems.motor.MotorConfig
import asiankoala.junk.v2.subsystems.motor.MotorControlType
import asiankoala.junk.v2.subsystems.motor.MotorSubsystem
import asiankoala.junk.v2.subsystems.motor.MotorSubsystemConfig
import com.qualcomm.robotcore.hardware.DcMotor

object Spinner : MotorSubsystem(
    MotorSubsystemConfig(MotorConfig("duckSpinner", zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE), MotorControlType.OPEN_LOOP)
) {
    fun setPower(power: Double) {
        output = power
    }
}
