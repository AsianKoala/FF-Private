package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import org.openftc.revextensions2.ExpansionHubServo
import robotuprising.lib.hardware.Status
import robotuprising.lib.opmode.AkemiDashboard
import robotuprising.lib.system.Subsystem

object BDS : Subsystem() {
    private lateinit var outsideLinkage: ExpansionHubServo
    private lateinit var insideLinkage: ExpansionHubServo

    // TODO
    private const val INNER_OUT_POS = Double.NaN
    private const val INNER_IN_POS = Double.NaN
    private const val OUTER_OUT_POS = Double.NaN
    private const val OUTER_IN_POS = Double.NaN

    // bcz i like using pairs
    var positions = Pair(INNER_IN_POS, OUTER_IN_POS)

    private var bdsState = BDSStates.RESTING
    private enum class BDSStates {
        RESTING,
        INTAKING,
        OUTTAKING
    }

    private var changeRequested = false

    fun intake() {
        bdsState = BDSStates.INTAKING
    }

    fun outtake() {
        bdsState = BDSStates.OUTTAKING
    }

    fun rest() {
        bdsState = BDSStates.RESTING
    }

    override fun init(hwMap: HardwareMap) {
        outsideLinkage = hwMap[ExpansionHubServo::class.java, "outsideLink"]
        insideLinkage = hwMap[ExpansionHubServo::class.java, "insideLink"]
    }

    override fun update() {
        if (changeRequested) {
            positions = when (bdsState) {
                BDSStates.RESTING -> INNER_IN_POS to OUTER_IN_POS
                BDSStates.INTAKING -> INNER_OUT_POS to OUTER_IN_POS
                BDSStates.OUTTAKING -> INNER_IN_POS to OUTER_OUT_POS
            }
            insideLinkage.position = positions.first
            outsideLinkage.position = positions.second
            changeRequested = false
        }
    }

    override fun stop() {
    }

    override fun sendDashboardPacket() {
        AkemiDashboard.addData("inner in pos", INNER_IN_POS)
        AkemiDashboard.addData("inner out pos", INNER_OUT_POS)
        AkemiDashboard.addData("outer in pos", OUTER_IN_POS)
        AkemiDashboard.addData("outer out pos", OUTER_OUT_POS)
        AkemiDashboard.addData("bds state", bdsState)
        AkemiDashboard.addData("positions pair", positions)
    }

    override var status: Status = Status.ALIVE
}
