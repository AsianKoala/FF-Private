package robotuprising.ftc2021.subsystems.osiris.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import robotuprising.ftc2021.subsystems.osiris.motor.*
import robotuprising.ftc2021.util.Constants

object Turret : ZeroableMotorSubsystem(
        MotorSubsystemConfig(
                MotorConfig(
                        "turret",
                        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
                ),

                controlType = MotorControlType.POSITION_PID,

                kP = 0.0005,

                ticksPerUnit = 5.33333,

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


