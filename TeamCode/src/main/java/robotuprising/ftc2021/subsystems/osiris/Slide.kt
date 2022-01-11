package robotuprising.ftc2021.subsystems.osiris

import com.qualcomm.robotcore.hardware.DcMotor
import robotuprising.ftc2021.subsystems.osiris.motor.MotorData
import robotuprising.ftc2021.subsystems.osiris.motor.*
import robotuprising.ftc2021.util.*
import robotuprising.lib.math.MathUtil

object Slide : ZeroableMotorSubsystem(
        MotorSubsystemConfig(
                MotorConfig(
                        "turret",
                        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
                ),

                MotorControlType.MOTION_PROFILE,

                0.0,
                (1.0 / MotorData.GB_13_7.ticksPerRev) * MathUtil.TAU * Constants.slideSpoolRadius,
                1.0,

                0.0,
                0.0,
                0.0,
                0.001,
                1 / 10.0,
                0.003,
                { _, _ -> 0.0 },

                10.0,
                5.0,
                0.0,

                0.1,

                0.25,
                0.0,
                180.0 / 25.4,

                Constants.slidePostZeroValue
        )
) {
    val slideInches get() = position * Constants.slideStages

    // target is in continuous form
    fun setSlideInches(target: Double) {
        generateAndFollowMotionProfile(position, target / Constants.slideStages)
    }
}