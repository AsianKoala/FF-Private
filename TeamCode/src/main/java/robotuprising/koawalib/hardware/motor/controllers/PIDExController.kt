package robotuprising.koawalib.hardware.motor.controllers

import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.control.PIDFController
import kotlin.math.absoluteValue

open class PIDExController(private val config: PIDFConfig) : Controller() {
    val controller: PIDFController = PIDFController(
            PIDCoefficients(config.kP, config.kI, config.kD),
            config.kV,
            config.kA,
            config.kStatic,
            config.kF
    )

    var targetPosition = 0.0
        private set
    private var targetVelocity = 0.0
    private var targetAcceleration = 0.0

    var currentPosition: Double = 0.0
        private set
    var currentVelocity: Double? = 0.0
        private set


    val isAtTarget get() = (currentPosition - targetPosition).absoluteValue < config.positionEpsilon
    val isHomed get() = !config.homePositionToDisable.isNaN() &&
            (targetPosition - config.homePositionToDisable).absoluteValue < config.positionEpsilon &&
            (config.homePositionToDisable - currentPosition).absoluteValue < config.positionEpsilon


    fun measure(position: Double, velocity: Double) {
        currentPosition = position
        currentVelocity = velocity
    }

    fun resetController() {
        controller.reset()
    }

    fun setControllerTargets(targetP: Double, targetV: Double = 0.0, targetA: Double = 0.0) {
        controller.targetPosition = targetP
        controller.targetVelocity = targetV
        controller.targetAcceleration = targetA

        targetPosition = targetP
        targetVelocity = targetV
        targetAcceleration = targetA
    }

    override fun process(): Double {
        return if(isHomed) {
            0.0
        } else {
            controller.update(currentPosition, currentVelocity)
        }
    }
}
