package robotuprising.ftc2021.subsystems.wraith

import robotuprising.ftc2021.hardware.wraith.MotorConfig
import robotuprising.ftc2021.subsystems.wraith.motor.MotorControlType
import robotuprising.ftc2021.subsystems.wraith.motor.MotorSubsystem
import robotuprising.ftc2021.subsystems.wraith.motor.MotorSubsystemConfig

object Turret : MotorSubsystem(
        MotorSubsystemConfig(
                "turret",

                MotorControlType.MOTION_PROFILE,

                0.0,
                (1 / MotorConfig.GB_13_7.ticksPerRev) * 360.0, // deg
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
                -90.0
        )
) {
    val turretAngle: Double get() = position

    fun setTurretAngle(angle: Double) {
        startFollowingMotionProfile(turretAngle, angle)
    }
}


