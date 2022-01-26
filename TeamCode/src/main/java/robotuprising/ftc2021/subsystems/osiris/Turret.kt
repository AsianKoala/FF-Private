package robotuprising.ftc2021.subsystems.osiris

import robotuprising.ftc2021.hardware.osiris.interfaces.Initializable
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
                (1 / MotorData.GB_5_2.ticksPerRev) * 360.0, // deg
                1.0 / 5.0,

                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
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

    fun setTurretProfileTarget(angle: Double) {
        generateAndFollowMotionProfile(turretAngle, angle)
    }

    fun setTurretLockAngle(angle: Double) {
        setControllerTarget(angle)
    }

}


