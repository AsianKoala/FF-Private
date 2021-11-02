package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.lib.control.motion.PIDCoeffs
import robotuprising.lib.control.motion.PIDFController
import robotuprising.lib.opmode.AkemiDashboard
import robotuprising.lib.system.Subsystem
import robotuprising.lib.util.Extensions.d

class Lift : Subsystem() {
    companion object {
        const val MAX_LIFT_STAGE = 4
        const val MIN_LIFT_STAGE = 0

        private const val MAX_ACCEL: Double = Double.NaN // todo
        private const val MAX_VEL: Double = Double.NaN // todo

        private val pidCoeffs = PIDCoeffs(1.0, 0.0, 0.0)
    }

    private val left = NakiriMotor("leftSlide", false)
    private val right = NakiriMotor("rightSlide", false)

    private var liftState = LiftStages.RESTING
    private var defaultLiftTarget = LiftStages.RESTING
    enum class LiftStages {
        RESTING,
        SHARED,
        ALLIANCE_LOW,
        ALLIANCE_MEDIUM,
        ALLIANCE_HIGH
    }

    private val targetPosition: Int
        get() = when (liftState) {
            LiftStages.RESTING -> 0 //              todo
            LiftStages.SHARED -> 100 //             todo
            LiftStages.ALLIANCE_LOW -> 175 //       todo
            LiftStages.ALLIANCE_MEDIUM -> 250 //    todo
            LiftStages.ALLIANCE_HIGH -> 500 //      todo
        }

    private val currPosition get() = left.position
    private var internalPower: Double = 0.d

    private val controller = PIDFController(pidCoeffs) // todo

    fun setLevel(stage: LiftStages) {
        liftState = stage
        controller.reset()
        controller.targetPosition = targetPosition.toDouble()
    }

    fun setLevelToDefault() {
        setLevel(defaultLiftTarget)
    }

    fun changeDefault(stage: LiftStages) {
        defaultLiftTarget = stage
    }

    private var emergency = false
    fun emergencyControl(power: Double) {
        emergency = true
        internalPower = power
        controller.reset()
    }

    override fun init(hwMap: HardwareMap) {
        left.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        left.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        controller.reset()
        controller.targetPosition = targetPosition.d
        controller.targetVelocity = MAX_VEL
        controller.targetAcceleration = MAX_ACCEL
    }

    override fun update() {
        if(!emergency) {
            internalPower = controller.update(currPosition.d)

        }

        left.power = internalPower
        right.power = internalPower
    }

    override fun stop() {
        controller.reset()
        internalPower = 0.d
        left.power = 0.d
    }

    override fun sendDashboardPacket() {
        AkemiDashboard["lift state"] = liftState
        AkemiDashboard["lift pid coeffs"] = pidCoeffs
        AkemiDashboard["lift internal power"] = internalPower
        AkemiDashboard["curr position"] = currPosition
        AkemiDashboard["target position"] = targetPosition
    }
}
