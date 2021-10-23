package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import org.openftc.revextensions2.ExpansionHubMotor
import robotuprising.lib.hardware.MecanumPowers
import robotuprising.lib.hardware.Status
import robotuprising.lib.opmode.AkemiDashboard
import robotuprising.lib.system.Subsystem
import robotuprising.lib.util.Extensions.d
import kotlin.math.PI
import kotlin.math.absoluteValue

object Homura : Subsystem() {
    private lateinit var motors: MutableList<ExpansionHubMotor>

    private const val MIN_THRESH = 0.01

    private const val WHEEL_RADIUS = 1.88976
    private const val GEAR_RATIO = 1.0
    private const val TICKS_PER_REV = 537.7

    private var internalPowers = mutableListOf(0.d, 0.d, 0.d, 0.d)
    private val appliedPowers = DoubleArray(4)
    private val prevAppliedPowers = DoubleArray(4)

    private fun ticksToInches(ticks: Int): Double {
        return WHEEL_RADIUS * 2 * PI * GEAR_RATIO * ticks / TICKS_PER_REV
    }

    val wheelPositions: List<Double> = motors.map { ticksToInches(it.currentPosition) }

    fun setFromMecanumPowers(powers: MecanumPowers) {
        internalPowers = powers.toMotor
    }

    fun setDirectPowers(powers: MutableList<Double>) {
        powers.forEachIndexed { i, it -> internalPowers[i] = it }
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
//        val newMotors = internalPowers.toMotor
        val max = internalPowers.map { it.absoluteValue }.maxOrNull()!!
        if (max > 1.0) {
            for (i in 0..4)
                internalPowers[i] /= max
        }

        val difference = internalPowers.mapIndexed { i, x -> (x - prevAppliedPowers[i]).absoluteValue }
        if (difference.maxOrNull()!! > MIN_THRESH) {
            for (i in 0..4) {
                appliedPowers[i] = internalPowers[i]
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
        setFromMecanumPowers(MecanumPowers())
    }

    override var status: Status = Status.ALIVE
}
