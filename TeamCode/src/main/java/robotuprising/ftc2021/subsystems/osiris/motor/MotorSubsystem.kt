package robotuprising.ftc2021.subsystems.osiris.motor

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.control.PIDFController
import com.acmerobotics.roadrunner.profile.MotionProfile
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator
import com.acmerobotics.roadrunner.profile.MotionState
import com.acmerobotics.roadrunner.util.epsilonEquals
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime
import robotuprising.ftc2021.hardware.osiris.interfaces.Loopable
import robotuprising.ftc2021.hardware.osiris.interfaces.Readable
import robotuprising.ftc2021.hardware.osiris.interfaces.Testable
import robotuprising.ftc2021.hardware.osiris.OsirisMotor
import robotuprising.ftc2021.hardware.osiris.interfaces.Initializable
import robotuprising.ftc2021.subsystems.osiris.Subsystem
import robotuprising.lib.math.MathUtil.epsilonNotEqual
import robotuprising.lib.opmode.OsirisDashboard
import robotuprising.lib.util.Extensions.d
import java.lang.Exception
import kotlin.math.absoluteValue

@Config
open class MotorSubsystem(val config: MotorSubsystemConfig) : Subsystem(), Initializable, Loopable, Readable, Testable {
    protected val motor: OsirisMotor by lazy { OsirisMotor(config.motorConfig) }

    private val controller: PIDFController by lazy {
         PIDFController(
            PIDCoefficients(config.kP, config.kI, config.kD),
            config.kV,
            config.kA,
            config.kStatic,
            config.kF
        )
    }

    private var targetPosition = 0.0

    protected var rawPosition = 0.0
    protected var rawVelocity = 0.0


    protected var position = 0.0
    protected var velocity = 0.0
    protected var output = 0.0

    var disabled = false


    // when we are at rest, don't want any motor movement
//    private val dead get() = targetPosition epsilonEquals config.homePosition || disabled

    val isAtTarget get() = (position - targetPosition).absoluteValue < config.positionEpsilon


    // pid
    protected fun setControllerTarget(target: Double) {
        controller.reset()
        controller.targetPosition = target
        targetPosition = target
    }

    private var followingPositionPID = false

    // motion profiling
    private var motionTimer = ElapsedTime()
    private var currentMotionProfile: MotionProfile? = null
    private var currentMotionState: MotionState? = null

    private var hasFinishedProfile = false

    protected fun ticksToUnits(ticks: Double): Double {
        return (ticks / config.ticksPerUnit) * config.gearRatio
    }

    private fun PIDFController.targetMotionState(state: MotionState) {
        targetPosition = state.x
        targetVelocity = state.v
        targetAcceleration = state.a
    }

    protected fun generateAndFollowMotionProfile(start: Double, end: Double) {
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

    protected fun followMotionProfile(profile: MotionProfile) {
        currentMotionProfile = profile

        hasFinishedProfile = false
        controller.reset()
        motionTimer.reset()
    }

    override fun stop() {

    }

    override fun updateDashboard(debugging: Boolean) {
        if(config.controlType != MotorControlType.OPEN_LOOP) {
            OsirisDashboard.addLine()
            OsirisDashboard.addLine(config.motorConfig.name)
//            OsirisDashboard["raw position"] = rawPosition
//            OsirisDashboard["raw velocity"] = rawVelocity
            OsirisDashboard["target position"] = targetPosition
            OsirisDashboard["position"] = position
            OsirisDashboard["output"] = output
//            OsirisDashboard["is at target"] = isAtTarget
            OsirisDashboard["disabled"] = disabled
            OsirisDashboard["sim output"] = simOutput
        }
    }

    var simOutput = 0.0
    override fun loop() {
        if(motor.mode == DcMotor.RunMode.STOP_AND_RESET_ENCODER) {
            motor.mode = config.motorConfig.mode
        }

        if(config.controlType != MotorControlType.OPEN_LOOP) {
            output = if(disabled) {
                simOutput = controller.update(position)
                0.0
            } else {
                if(config.controlType == MotorControlType.MOTION_PROFILE && !hasFinishedProfile) {
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
                }

                val rawOutput = if(disabled) {
                    0.0
                } else if(!config.homePositionToDisable.isNaN() &&
                        (targetPosition - config.homePositionToDisable).absoluteValue < config.positionEpsilon &&
                        (config.homePositionToDisable - position).absoluteValue < config.positionEpsilon) {
                    0.0
                } else {
                    controller.update(position)
                }


                rawOutput
            }
        }

        motor.power = output
    }

    override fun read() {
        if(config.controlType != MotorControlType.OPEN_LOOP) {
            rawPosition = motor.position.d
            rawVelocity = motor.velocity

            position = ticksToUnits(rawPosition)
            velocity = ticksToUnits(rawVelocity)
        }
    }

    override fun test() {
        motor.power = 0.1
    }

    override fun init() {
        disabled = false
        hasFinishedProfile = true
        targetPosition = 0.0
        output = 0.0
        controller.reset()
        motionTimer.reset()
        currentMotionProfile = null
        currentMotionState = null
        rawPosition = 0.0
        rawVelocity = 0.0
        position = 0.0
        velocity = 0.0
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    }
}

/*

position = raw - offset + if(zeroed) postZeroed

raw = 100
offset = 0
postZeroed = 0

position = 100

(zeroed)

raw = 100
offset = 100
postZeroed = 50

position = 100 - 100 + 50
position = 50

raw = 200
offset = 100
postZeroed = 50

(zeroed)

raw = 200
offset = 200





 */