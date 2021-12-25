package robotuprising.ftc2021.subsystems.wraith.motor

import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.control.PIDFController
import com.acmerobotics.roadrunner.profile.MotionProfile
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator
import com.acmerobotics.roadrunner.profile.MotionState
import com.acmerobotics.roadrunner.util.epsilonEquals
import com.qualcomm.robotcore.util.ElapsedTime
import robotuprising.ftc2021.hardware.wraith.interfaces.Loopable
import robotuprising.ftc2021.hardware.wraith.interfaces.Readable
import robotuprising.ftc2021.hardware.wraith.interfaces.Testable
import robotuprising.ftc2021.hardware.wraith.WraithMotor
import robotuprising.ftc2021.subsystems.wraith.Subsystem
import robotuprising.lib.math.MathUtil
import robotuprising.lib.opmode.WraithDashboard
import robotuprising.lib.util.Extensions.d
import java.lang.Exception
import kotlin.math.absoluteValue

open class MotorSubsystem(var config: MotorSubsystemConfig) : Subsystem(), Loopable, Readable, Testable {

    protected val motor = WraithMotor(config.name, false)

    private val controller by lazy { PIDFController(
            PIDCoefficients(config.kP, config.kI, config.kD),
            config.kV,
            config.kA,
            config.kStatic,
            config.kF
    ) }

    private var targetPosition = 0.0

    private var rawPosition = 0.0
    private var rawVelocity = 0.0

    protected var position = 0.0
    protected var velocity = 0.0
    protected var output = 0.0

    // when we are at rest, don't want any motor movement
    private val dead get() = position.absoluteValue < config.deadzone && targetPosition epsilonEquals config.homePosition

    protected val isAtTarget get() = (position - targetPosition).absoluteValue < config.positionEpsilon

    private fun ticksToUnits(ticks: Double): Double {
        return (ticks / config.unitsPerTick) * config.gearRatio
    }

    // motion profiling
    private var motionTimer = ElapsedTime()
    private var currentMotionProfile: MotionProfile? = null
    private var currentMotionState: MotionState? = null

    private var hasFinishedProfile = true

    protected fun startFollowingMotionProfile(start: Double, end: Double) {
        val startState = MotionState(start, 0.0)
        val endState = MotionState(end, 0.0)

        currentMotionProfile = MotionProfileGenerator.generateSimpleMotionProfile(
                startState,
                endState,
                config.maxVelocity,
                config.maxAcceleration,
                config.maxJerk
        )

        hasFinishedProfile = false
        controller.reset()
        motionTimer.reset()
    }

    private fun PIDFController.targetMotionState(state: MotionState) {
        targetPosition = state.x
        targetVelocity = state.v
        targetAcceleration = state.a
    }

    override fun reset() {
        targetPosition = 0.0
        output = 0.0
        controller.reset()
        currentMotionState = MotionState(0.0, 0.0)
    }

    override fun updateDashboard(debugging: Boolean) {
        if(debugging) {
            WraithDashboard["motor config"] = config
        }
    }

    override fun loop() {
        if(config.controlType != MotorControlType.OPEN_LOOP) {
            output = if(dead) {
                0.0
            } else if(config.controlType == MotorControlType.MOTION_PROFILE && !hasFinishedProfile){
                when {
                    currentMotionProfile == null -> throw Exception("MUST BE FOLLOWING A MOTION PROFILE!!!")

                    motionTimer.seconds() > currentMotionProfile!!.duration() -> {
                        hasFinishedProfile = true
                        currentMotionProfile = null
                        currentMotionState = null
                    }

                    else -> {
                        currentMotionState = currentMotionProfile!![motionTimer.seconds()]

                        targetPosition = currentMotionState!!.x
                        controller.targetMotionState(currentMotionState!!)
                    }
                }

                controller.update(position, velocity)
            } else {
                controller.update(position, velocity)
            }
        }


        if(position - config.positionEpsilon < config.positionLowerLimit) {
            output = MathUtil.clamp(output, 0.0, 1.0)
        }

        if(position + config.positionUpperLimit > config.positionUpperLimit) {
            output = MathUtil.clamp(output, -1.0, 0.0)
        }

        motor.power = output
    }

    override fun read() {
        rawPosition = motor.position.d
        rawVelocity = motor.velocity

        position = ticksToUnits(rawPosition)
        velocity = ticksToUnits((rawVelocity))
    }

    override fun test() {
        motor.power = 0.05
    }
}
