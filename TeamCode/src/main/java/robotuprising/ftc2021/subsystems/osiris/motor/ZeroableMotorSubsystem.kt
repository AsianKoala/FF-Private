package robotuprising.ftc2021.subsystems.osiris.motor

import com.qualcomm.robotcore.hardware.DcMotor
import robotuprising.ftc2021.hardware.osiris.interfaces.Zeroable
import robotuprising.lib.util.Extensions.d

open class ZeroableMotorSubsystem(config: MotorSubsystemConfig) : MotorSubsystem(config), Zeroable {
    override val zeroed: Boolean get() = zeroedYet

    private var zeroedYet = false
    private var offset = 0.0
        set(value) {
            if(!zeroedYet) {
                zeroedYet = true
            }

            field = value
        }

    override fun switchToFloatForZero() {
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
    }

    override fun zero() {
        offset = rawPosition

        if(config.motorConfig.zeroPowerBehavior == DcMotor.ZeroPowerBehavior.BRAKE) {
            motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        }
        zeroedYet = true
    }

    override fun read() {
        if(config.controlType != MotorControlType.OPEN_LOOP) {
            rawPosition = motor.position.d - offset
            rawVelocity = motor.velocity

            position = ticksToUnits(rawPosition) + if(zeroedYet) config.postZeroedValue else 0.0
            velocity = ticksToUnits(rawVelocity)

        }
    }
}