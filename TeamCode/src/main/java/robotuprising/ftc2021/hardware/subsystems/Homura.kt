package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.lib.system.Subsystem
import robotuprising.lib.opmode.AkemiDashboard
import org.openftc.revextensions2.ExpansionHubMotor
import robotuprising.lib.hardware.Accuracy
import robotuprising.lib.hardware.MecanumPowers
import robotuprising.lib.hardware.Status
import kotlin.math.absoluteValue

object Homura : Subsystem() {

    private lateinit var motors: MutableList<ExpansionHubMotor>

    private val appliedPowers = DoubleArray(4)
    private val prevAppliedPowers = DoubleArray(4)

    private val minThresh = 0.01

    private var internalPowers: MecanumPowers = MecanumPowers()

    fun setPowers(powers: MecanumPowers) {
        internalPowers.set(powers)
    }

    override fun init(hwMap: HardwareMap) {
        motors = mutableListOf(
            hwMap[ExpansionHubMotor::class.java, "FL"],
            hwMap[ExpansionHubMotor::class.java, "BL"],
            hwMap[ExpansionHubMotor::class.java, "FR"],
            hwMap[ExpansionHubMotor::class.java, "BR"]
        )
    }

    override fun update() {
        val newMotors = internalPowers.toMotor
        val max = newMotors.map { it.absoluteValue }.maxOrNull()!!
        if(max > 1.0) {
            for(i in 0..4)
                newMotors[i] /= max
        }

        val difference = newMotors.mapIndexed { i, x -> (x - prevAppliedPowers[i]).absoluteValue }
        if (difference.maxOrNull()!! > minThresh) {
            for(i in 0..4) {
                appliedPowers[i] = newMotors[i]
                motors[i].power = appliedPowers[i]
                prevAppliedPowers[i] = appliedPowers[i]
            }
        }
    }

    override fun sendDashboardPacket() {
        val r = HashMap<String, Any>()
        r["fl"] = appliedPowers[0]
        r["bl"] = appliedPowers[1]
        r["fr"] = appliedPowers[2]
        r["br"] = appliedPowers[3]
        AkemiDashboard.addAll(r)
    }

    override fun stop() {
        setPowers(MecanumPowers())
    }

    override var status: Status = Status.ALIVE
    override var acc: Accuracy = Accuracy.HIGH
}
