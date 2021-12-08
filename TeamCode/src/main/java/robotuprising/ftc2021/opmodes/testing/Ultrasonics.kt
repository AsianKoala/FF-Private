package robotuprising.ftc2021.opmodes.testing

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

    //Timer object for the delay
    private val delayTimer = ElapsedTime()

    //Delay in ms between pings,
    private val readingDelayMs = 80

    //Whether we should be active
    private var takingRangeReading = false
    var counter = 0
    //Last read range in cm
    private var forwardRangeReading = 0.0
    private var horizRangeReading = 0.0
    private var firstRun = true

    fun getForwardRange(unit: DistanceUnit?): Double {
        return forwardRangeReading
    }

    //Start taking range readings
    fun startReading() {
        takingRangeReading = true
    }

    //Stop taking range readings
    fun stopReading() {
        takingRangeReading = false
        firstRun = true
    }

    //Tells if the delay timer has expired or not
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
                horizSensor.ping() //todo check if i2c call needed for ultrasonics after read
                delayTimer.reset()
                counter++
                //If its the first run then run a range command to ensure the next reading has a value
            } else if (firstRun) {
                horizSensor.ping()
                forwardSensor.ping()
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