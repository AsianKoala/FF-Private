package robotuprising.ftc2021.subsystems.nakiri

import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.util.MB1242
import robotuprising.ftc2021.util.BulkDataManager
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem

class Ultrasonics : Subsystem {
    //forward sensor
    private val forwardSensor: MB1242 = BulkDataManager.hwMap[MB1242::class.java, "forward"]
    private val horizSensor: MB1242 = BulkDataManager.hwMap[MB1242::class.java, "horiz"]

    private val delayTimer = ElapsedTime()

    private val readingDelayMs = 80

    //Whether we should be active
    private var takingRangeReading = false
    var counter = 0

    // mm
    var forwardRangeReading = 0.0
        private set
    var horizRangeReading = 0.0
        private set
    private var firstRun = true

    fun startReading() {
        takingRangeReading = true
    }

    fun stopReading() {
        takingRangeReading = false
        firstRun = true
    }

    private fun hasDelayExpired(): Boolean {
        return delayTimer.milliseconds() >= readingDelayMs
    }

    override fun update() {
        //Only take readings if we are supposed to, it's an unnecessary hardware call otherwise
        if (takingRangeReading) {
            //Check if its been longer than our delay to properly let the sensor perform
            if (hasDelayExpired() && !firstRun) {
                forwardRangeReading = forwardSensor.getDistance(DistanceUnit.CM)
                horizRangeReading = horizSensor.getDistance(DistanceUnit.CM)
                forwardSensor.ping()
                horizSensor.ping()
                delayTimer.reset()
                counter++
                //If its the first run then run a range command to ensure the next reading has a value
            } else if (firstRun) {
                forwardSensor.ping()
                horizSensor.ping()
                delayTimer.reset()
                firstRun = false
            }
        }
    }

    override fun sendDashboardPacket(debugging: Boolean) {
        NakiriDashboard["taking range reading"] = takingRangeReading
        NakiriDashboard["counter"] = counter
        NakiriDashboard["forward reading"] = forwardRangeReading
        NakiriDashboard["horiz reading"] = horizRangeReading
    }

    override fun reset() {
        stopReading()
    }
}