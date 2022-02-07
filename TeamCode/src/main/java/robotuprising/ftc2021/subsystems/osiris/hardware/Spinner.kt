package robotuprising.ftc2021.subsystems.osiris.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import robotuprising.ftc2021.subsystems.osiris.motor.*


object Spinner : MotorSubsystem(
        MotorSubsystemConfig(MotorConfig("duckSpinner", zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE), MotorControlType.OPEN_LOOP)
) {
        fun setPower(power: Double) {
                output = power
        }


}