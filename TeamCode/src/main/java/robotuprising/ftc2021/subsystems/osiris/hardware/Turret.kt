package robotuprising.ftc2021.subsystems.osiris.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import robotuprising.ftc2021.subsystems.osiris.motor.MotorData
import robotuprising.ftc2021.subsystems.osiris.motor.*
import robotuprising.ftc2021.util.Constants

object Turret : ZeroableMotorSubsystem(
        MotorSubsystemConfig(
                MotorConfig(
                        "turret",
                        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
                ),

                controlType = MotorControlType.MOTION_PROFILE,

                homePosition = Constants.turretPostZeroValue,
                unitsPerTick = (1 / MotorData.GB_13_7.ticksPerRev) * 360.0, // deg
                gearRatio = 1.0 / 5.0,

                postZeroedValue = Constants.turretPostZeroValue
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


