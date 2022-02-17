package robotuprising.ftc2021.v2.subsystems.osiris.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import robotuprising.ftc2021.v2.subsystems.osiris.motor.*


object Spinner : MotorSubsystem(
        MotorSubsystemConfig(MotorConfig("duckSpinner", zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE), MotorControlType.OPEN_LOOP)
) {
        fun setPower(power: Double) {
                output = power
        }


}