package robotuprising.ftc2021.subsystems.wraith

import com.qualcomm.robotcore.util.ElapsedTime
import robotuprising.ftc2021.hardware.wraith.MotorConfig
import robotuprising.ftc2021.hardware.wraith.interfaces.Zeroable
import robotuprising.ftc2021.subsystems.wraith.motor.MotorControlType
import robotuprising.ftc2021.subsystems.wraith.motor.MotorSubsystem
import robotuprising.ftc2021.subsystems.wraith.motor.MotorSubsystemConfig
import robotuprising.ftc2021.util.*
import robotuprising.lib.math.MathUtil

object Slide : MotorSubsystem(
        MotorSubsystemConfig(
                "slide",

                MotorControlType.MOTION_PROFILE,

                0.0,
                (1.0 / MotorConfig.GB_13_7.ticksPerRev) * MathUtil.TAU * Constants.slideSpoolRadius,
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
                180.0 / 25.4
        )
), Zeroable {
    val slideInches get() = position * Constants.slideStages

    // target is in continuous form
    fun setSlideInches(target: Double) {
        generateAndFollowMotionProfile(position, target / Constants.slideStages)
    }

    override val zeroInitTime: Long = 0
    override val postOffsetValue: Double = Constants.turretZeroValue
    override var offset: Double = 0.0

    override fun zero() {

    }

    override fun readyForInit(): Boolean {
        TODO("Not yet implemented")
    }

}