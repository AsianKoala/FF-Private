package robotuprising.ftc2021.hardware.osiris

import com.qualcomm.robotcore.hardware.DcMotorSimple

import com.acmerobotics.roadrunner.util.NanoClock

import com.qualcomm.robotcore.hardware.DcMotorEx

// todo fix
class OsirisEncoder constructor(private val motor: DcMotorEx, private val clock: NanoClock = NanoClock.system()) {
    enum class Direction(val multiplier: Int) {
        FORWARD(1), REVERSE(-1);

    }

    /**
     * Allows you to set the direction of the counts and velocity without modifying the motor's direction state
     * @param direction either reverse or forward depending on if encoder counts should be negated
     */
    var direction: Direction
    private var lastPosition: Int
    private var velocityEstimate: Double
    private var lastUpdateTime: Double
    private val multiplier: Int
        private get() = direction.multiplier * if (motor.direction == DcMotorSimple.Direction.FORWARD) 1 else -1
    val currentPosition: Int
        get() {
            val multiplier = multiplier
            val currentPosition = motor.currentPosition * multiplier
            if (currentPosition != lastPosition) {
                val currentTime = clock.seconds()
                val dt = currentTime - lastUpdateTime
                velocityEstimate = (currentPosition - lastPosition) / dt
                lastPosition = currentPosition
                lastUpdateTime = currentTime
            }
            return currentPosition
        }
    val rawVelocity: Double
        get() {
            val multiplier = multiplier
            return motor.velocity * multiplier
        }
    val correctedVelocity: Double
        get() = inverseOverflow(rawVelocity, velocityEstimate)

    companion object {
        private const val CPS_STEP = 0x10000
        private fun inverseOverflow(input: Double, estimate: Double): Double {
            var real = input
            while (Math.abs(estimate - real) > CPS_STEP / 2.0) {
                real += Math.signum(estimate - real) * CPS_STEP
            }
            return real
        }
    }

    init {
        direction = Direction.FORWARD
        lastPosition = 0
        velocityEstimate = 0.0
        lastUpdateTime = clock.seconds()
    }
}
