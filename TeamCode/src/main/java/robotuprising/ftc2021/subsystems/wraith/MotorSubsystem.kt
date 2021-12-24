package robotuprising.ftc2021.subsystems.wraith

import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.control.PIDFController
import com.acmerobotics.roadrunner.profile.MotionState
import com.acmerobotics.roadrunner.util.epsilonEquals
import robotuprising.ftc2021.util.Loopable
import robotuprising.ftc2021.util.Readable
import robotuprising.ftc2021.util.WraithMotor
import robotuprising.lib.util.Extensions.d
import kotlin.math.absoluteValue

abstract class MotorSubsystem(var config: ControlledMotorConfig) : Subsystem(), Loopable, Readable {

    protected val motor = WraithMotor(config.name, config.onMaster)

    protected val controller by lazy { PIDFController(
            PIDCoefficients(config.kP, config.kI, config.kD),
            config.kV,
            config.kA,
            config.kStatic,
            config.kF
    ) }

    private var targetPosition = 0.0

    protected var position = 0.0
    protected var velocity = 0.0
    protected var output = 0.0

    // the motion state fed into PIDF controller
    private var currentMotionState = MotionState(0.0, 0.0)

    // when we are at rest, don't want any motor movement
    private val dead get() = position < config.deadzone && targetPosition epsilonEquals config.homePosition

    protected val isAtTarget get() = (position - targetPosition).absoluteValue < config.positionEpsilon

    override fun reset() {
        targetPosition = 0.0
        output = 0.0
        controller.reset()
        currentMotionState = MotionState(0.0, 0.0)
    }

    override fun updateDashboard() {

    }

    override fun loop() {
        motor.power = when(config.controlType) {
            MotorControlType.OPEN_LOOP -> output

            MotorControlType.POSITION_PID -> {
                if(dead) {
                    0.0
                } else {
                    output = controller.update(position, velocity)
                    output
                }
            }

            MotorControlType.MOTION_PROFILE -> {
                targetPosition = currentMotionState.x

                controller.targetPosition = targetPosition
                controller.targetVelocity = currentMotionState.v
                controller.targetAcceleration = currentMotionState.a

                output = controller.update(position, velocity)

                output
            }

        }
    }

    override fun read() {
        // TODO for now ill read from motor directly, but need to switch to bulk reading
        // or whatever the fuck recharged green is doing
        position = motor.position.d
        velocity = motor.velocity
    }
}
