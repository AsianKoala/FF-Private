package robotuprising.ftc2021.subsystems.wraith

import com.qualcomm.robotcore.util.ElapsedTime
import robotuprising.ftc2021.hardware.wraith.MotorConfig
import robotuprising.ftc2021.hardware.wraith.interfaces.Zeroable
import robotuprising.ftc2021.subsystems.wraith.motor.MotorControlType
import robotuprising.ftc2021.subsystems.wraith.motor.MotorSubsystem
import robotuprising.ftc2021.subsystems.wraith.motor.MotorSubsystemConfig
import robotuprising.ftc2021.util.Constants

object Turret : MotorSubsystem(
        MotorSubsystemConfig(
                "turret",

                MotorControlType.MOTION_PROFILE,

                90.0,
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
), Zeroable {
    val turretAngle: Double get() = position

    fun setTurretAngle(angle: Double) {
        generateAndFollowMotionProfile(turretAngle, angle)
    }

    // todo put this shit in motor subsystem
    override var zeroInitTime = -1
    override val postOffsetValue: Double = Constants.turretZeroValue
    override var offset: Double = 0.0

    override fun zero() {
        if(zeroInitTime == -1) {
            zeroInitTime = System.currentTimeMillis().toInt()
        } else {
            offset = position + postOffsetValue
        }
    }

    override fun readyForInit(): Boolean {
        TODO("Not yet implemented")
    }
}


