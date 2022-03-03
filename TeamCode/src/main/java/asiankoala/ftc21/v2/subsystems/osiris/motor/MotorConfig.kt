package asiankoala.ftc21.v2.subsystems.osiris.motor

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

/**
 * optional config constructor for OsirisMotor
 */
data class MotorConfig(
        val name: String,
        val mode: DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER,
        val zeroPowerBehavior: DcMotor.ZeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE,
        val direction: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD
)