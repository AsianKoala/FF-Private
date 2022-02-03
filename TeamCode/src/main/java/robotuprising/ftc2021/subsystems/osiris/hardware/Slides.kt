package robotuprising.ftc2021.subsystems.osiris.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import robotuprising.ftc2021.subsystems.osiris.motor.*
import robotuprising.ftc2021.util.*

object Slides : ZeroableMotorSubsystem(
        MotorSubsystemConfig(
                MotorConfig(
                        "slides",
                        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT,
                        direction = DcMotorSimple.Direction.REVERSE
                ),

                controlType = MotorControlType.POSITION_PID,

                ticksPerUnit = 1.0,
                gearRatio = 1.0,

                kP = 0.03,
                kD = 0.0001,


                postZeroedValue = Constants.slideHomeValue
        )
) {
    val slideInches get() = position

    // target is in continuous form
    fun setSlideInches(target: Double) {
        generateAndFollowMotionProfile(position, target / slideInches)
    }

    fun setSlideLockTarget(target: Double) {
        setControllerTarget(target)
    }
}