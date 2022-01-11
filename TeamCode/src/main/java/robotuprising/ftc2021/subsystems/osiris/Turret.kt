package robotuprising.ftc2021.subsystems.osiris

import robotuprising.ftc2021.subsystems.osiris.motor.MotorData
import robotuprising.ftc2021.subsystems.osiris.motor.*
import robotuprising.ftc2021.util.Constants

object Turret : ZeroableMotorSubsystem(
        MotorSubsystemConfig(
                MotorConfig(
                        "turret",
                ),

                MotorControlType.MOTION_PROFILE,

                90.0,
                (1 / MotorData.GB_13_7.ticksPerRev) * 360.0, // deg
                1.0 / 5.0,

                0.0,
                0.0,
                0.0,
                0.001,
                1 / 90.0,
                0.003,
                { _, _ -> 0.0 },

                90.0, // deg/s
                60.0, // deg/s^2
                0.0, // deg/s^3

                0.2,

                0.5,
                90.0,
                -90.0,

                Constants.turretPostZeroValue
        )
) {
    val turretAngle: Double get() = position

    fun setTurretAngle(angle: Double) {
        generateAndFollowMotionProfile(turretAngle, angle)
    }
}


