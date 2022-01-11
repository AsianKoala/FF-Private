package robotuprising.ftc2021.subsystems.osiris.motor

import com.qualcomm.robotcore.hardware.DcMotor
import robotuprising.ftc2021.hardware.osiris.interfaces.Zeroable

open class ZeroableMotorSubsystem(config: MotorSubsystemConfig) : MotorSubsystem(config), Zeroable {
    override val zeroed: Boolean get() = isZeroed

    override fun switchToFloatForZero() {
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
    }

    override fun zero() {
        offset = rawPosition
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        isZeroed = true
    }
}