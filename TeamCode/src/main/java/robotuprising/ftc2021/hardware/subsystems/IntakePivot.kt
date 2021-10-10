package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import org.openftc.revextensions2.ExpansionHubServo
import robotuprising.lib.hardware.Status
import robotuprising.lib.opmode.AkemiDashboard
import robotuprising.lib.system.Subsystem

object IntakePivot : Subsystem() {
    private lateinit var pivotServo: ExpansionHubServo

    const val RESTING = 1.0 // todo
    const val OUT = 0.0 // todo

    private var pivotState = PivotStates.RESTING
    enum class PivotStates {
        RESTING,
        OUT
    }

    private var requestedChange = false

    fun rotateBack() {
        pivotState = PivotStates.OUT
        requestedChange = true
    }

    fun rest() {
        pivotState = PivotStates.RESTING
        requestedChange = true
    }

    override fun init(hwMap: HardwareMap) {
        pivotServo = hwMap[ExpansionHubServo::class.java, "pivot"]
    }

    override fun update() {
        if (requestedChange) {
            requestedChange = false
            pivotServo.position = when (pivotState) {
                PivotStates.RESTING -> RESTING
                PivotStates.OUT -> OUT
            }
        }
    }

    override fun stop() {
    }

    override fun sendDashboardPacket() {
        AkemiDashboard.addData("pivot state", pivotState)
        AkemiDashboard.addData("resting val", RESTING)
        AkemiDashboard.addData("out val", OUT)
    }

    override var status: Status = Status.ALIVE
}
