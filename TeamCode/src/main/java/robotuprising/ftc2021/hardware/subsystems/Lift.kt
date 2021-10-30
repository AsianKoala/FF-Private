package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.openftc.revextensions2.ExpansionHubMotor
import robotuprising.lib.control.motion.PIDCoeffs
import robotuprising.lib.control.motion.PIDFController
import robotuprising.lib.hardware.Status
import robotuprising.lib.system.Subsystem
import robotuprising.lib.util.Extensions.d

object Lift : Subsystem() {

    private lateinit var leftLiftMotor: ExpansionHubMotor
    private lateinit var rightLiftMotor: ExpansionHubMotor

    private var liftState = LiftStages.RESTING
    const val MAX_LIFT_STAGE = 4
    const val MIN_LIFT_STAGE = 0
    enum class LiftStages {
        RESTING,
        SHARED,
        ALLIANCE_HIGH,
        ALLIANCE_MEDIUM,
        ALLIANCE_LOW
    }

    private var defaultLiftTarget = LiftStages.SHARED

    private var internalPower: Double = 0.d

    private lateinit var pidCoeffs: PIDCoeffs
    private val controller = PIDFController(pidCoeffs)

    private var currPosition: Int = 0
    private val targetPosition: Int
        get() = when (liftState) {
            LiftStages.RESTING -> MIN_LIFT_ENC
            LiftStages.SHARED -> 100
            LiftStages.ALLIANCE_HIGH -> 500
            LiftStages.ALLIANCE_MEDIUM -> 250
            LiftStages.ALLIANCE_LOW -> 175
        }

    private var controllerOutput: Double = 0.d

    // consts
    // TODO
    private const val MAX_LIFT_ENC: Int = Int.MAX_VALUE
    private const val MIN_LIFT_ENC: Int = Int.MIN_VALUE
    private const val MAX_ACCEL: Double = Double.NaN
    private const val MAX_VEL: Double = Double.NaN

    fun setLevel(stage: LiftStages) {
        liftState = stage
        controller.reset()
//        controller.setTargets(targetPosition.d, MAX_VEL, MAX_ACCEL)
    }

    fun goToDefault() {
        setLevel(defaultLiftTarget)
    }

    fun setDefaultTarget(stage: LiftStages) {
        defaultLiftTarget = stage
    }

    fun emergencyControl(power: Double) {
        status = Status.EMERGENCY
        internalPower = power
        controller.reset()
    }

    override fun init(hwMap: HardwareMap) {
        leftLiftMotor = hwMap[ExpansionHubMotor::class.java, "lift"]
        leftLiftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        leftLiftMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

//        controller.setBounds(1.0, -1.0)
    }

    override fun update() {
        if (status != Status.EMERGENCY) {
            currPosition = leftLiftMotor.currentPosition
            internalPower = controller.update(currPosition.d)
        }

        if (currPosition > MAX_LIFT_ENC - 5) {
            status = Status.EMERGENCY
            controller.reset()
            leftLiftMotor.power = 0.d
        }
        leftLiftMotor.power = internalPower
    }

    override fun stop() {
        controller.reset()
        internalPower = 0.d
        leftLiftMotor.power = 0.d
    }

    override fun sendDashboardPacket() {
//        AkemiDashboard.addData("lift state", liftState)
//        AkemiDashboard.addData("lift pid coeffs", pidCoeffs)
//        AkemiDashboard.addData("internal power", internalPower)
//        AkemiDashboard.addData("controller output", controllerOutput)
//        AkemiDashboard.addData("curr position", currPosition)
//        AkemiDashboard.addData("target position", targetPosition)
    }

    override var status: Status = Status.ALIVE
}
