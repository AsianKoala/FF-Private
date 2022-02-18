package robotuprising.koawalib.subsystem.motor

import robotuprising.koawalib.hardware.KMotor
import robotuprising.koawalib.math.MathUtil.d
import robotuprising.koawalib.subsystem.DeviceSubsystem
import robotuprising.koawalib.subsystem.motor.controllers.Controller
import robotuprising.koawalib.subsystem.motor.controllers.MPController
import robotuprising.koawalib.subsystem.motor.controllers.OpenLoopController
import robotuprising.koawalib.subsystem.motor.controllers.PIDExController
//import robotuprising.koawalib.util.Extensions.d

open class MotorSubsystem(motor: KMotor, private val controller: Controller) : DeviceSubsystem<KMotor>(motor) {

    fun setPower(power: Double) {
        if(controller is OpenLoopController) {
            controller.output = power
        }
    }

    fun setPIDTarget(targetPosition: Double, targetVelocity: Double, targetAcceleration: Double) {
        if(controller is PIDExController) {
            controller.setControllerTargets(targetPosition, targetVelocity, targetAcceleration)
        }
    }

    fun setMotionStateTarget(targetPosition: Double) {
        if(controller is MPController) {
            controller.generateAndFollowMotionProfile(controller.currentPosition, 0.0, targetPosition, 0.0)
        }
    }

    fun disable() {
        controller.disable()
    }

    fun enable() {
        controller.enable()
    }

    override fun periodic() {
        if(controller is PIDExController) {
            controller.measure(device.position.d, device.velocity)
        }

        device.power = controller.output
    }
}