package robotuprising.ftc2021.subsystems.osiris.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import robotuprising.ftc2021.subsystems.osiris.motor.MotorData
import robotuprising.ftc2021.subsystems.osiris.motor.*
import robotuprising.ftc2021.util.*
import robotuprising.lib.math.MathUtil

object Slides : ZeroableMotorSubsystem(
        MotorSubsystemConfig(
                MotorConfig(
                        "turret",
                        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
                ),

                controlType = MotorControlType.POSITION_PID,

                unitsPerTick = (1.0 / MotorData.GB_13_7.ticksPerRev) * MathUtil.TAU * Constants.slideSpoolRadius,
                gearRatio = 1.0,


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