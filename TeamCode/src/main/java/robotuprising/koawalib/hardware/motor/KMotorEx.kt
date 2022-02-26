package robotuprising.koawalib.hardware.motor

import robotuprising.koawalib.hardware.motor.controllers.Controller
import robotuprising.koawalib.hardware.motor.controllers.MPController
import robotuprising.koawalib.hardware.motor.controllers.OpenLoopController
import robotuprising.koawalib.hardware.motor.controllers.PIDExController

/**
 * Extended KMotor implementation
 * @see KMotor
 */
class KMotorEx(
        name: String,
        private val controller: Controller = OpenLoopController()
): KMotor(name) {

    override fun setSpeed(speed: Double) {
        if(controller is OpenLoopController) {
            controller.setDirectOutput(power)
        } else {
            throw IllegalArgumentException("MOTOR IS NOT OPEN LOOP")
        }
    }

    fun setPIDTarget(targetPosition: Double, targetVelocity: Double, targetAcceleration: Double) {
        if(controller is PIDExController) {
            controller.setControllerTargets(targetPosition, targetVelocity, targetAcceleration)
        } else {
            throw IllegalArgumentException("MOTOR IS NOT PID CONTROLLED")
        }
    }

    fun setMotionProfileTarget(targetPosition: Double) {
        if(controller is MPController) {
            controller.generateAndFollowMotionProfile(controller.currentPosition, 0.0, targetPosition, 0.0)
        } else {
            throw IllegalArgumentException("MOTOR IS NOT MOTION PROFILED")
        }
    }

    fun disable() {
        controller.disable()
    }

    fun enable() {
        controller.enable()
    }

    fun update() {
        if(controller is PIDExController) {
            controller.measure(position, velocity)
        }
        controller.update()
        super.setSpeed(controller.output)
    }
}