package robotuprising.ftc2021.subsystems.wraith

import robotuprising.ftc2021.hardware.wraith.MotorConfig
import robotuprising.ftc2021.subsystems.wraith.motor.MotorControlType
import robotuprising.ftc2021.subsystems.wraith.motor.MotorSubsystem
import robotuprising.ftc2021.subsystems.wraith.motor.MotorSubsystemConfig
import robotuprising.ftc2021.util.Constants
import robotuprising.lib.math.MathUtil


object Spinner : MotorSubsystem(
        MotorSubsystemConfig(
                "duckSpinner",

                MotorControlType.MOTION_PROFILE,

                0.0,
                (1.0 / MotorConfig.GB_5_2.ticksPerRev) * MathUtil.TAU * Constants.duckWheelDiameter,
                1.0 / Constants.carouselDiameter,

                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                {_, _ -> 0.0 },

                360.0,
                180.0,
                0.0,
        )
) {
        // todo implement stupid time based
}