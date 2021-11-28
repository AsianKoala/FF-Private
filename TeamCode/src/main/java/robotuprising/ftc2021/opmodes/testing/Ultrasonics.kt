package robotuprising.ftc2021.opmodes.testing

import com.qualcomm.hardware.rev.Rev2mDistanceSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.util.MB1242

class Ultrasonics(hardwareMap: HardwareMap) {
    //forward sensor
    private val forwardSensor: MB1242 = hardwareMap.get(MB1242::class.java, "forward")

    //Timer object for the delay
    private val delayTimer = ElapsedTime()

    //Delay in ms between pings,
    private val readingDelayMs = 80

    //Whether we should be active
    private var takingRangeReading = false
    var counter = 0
    //Last read range in cm
    private var forwardRangeReading = 0.0
    private var firstRun = true
    fun periodic() {
        //Only take readings if we are supposed to, it's an unnecessary hardware call otherwise
        if (takingRangeReading) {
            //Check if its been longer than our delay to properly let the sensor perform
            if (hasDelayExpired() && !firstRun) {
                forwardRangeReading = forwardSensor.getDistance(DistanceUnit.CM)
                forwardSensor.ping()
                delayTimer.reset()
                counter++
                //If its the first run then run a range command to ensure the next reading has a value
            } else if (firstRun) {
                forwardSensor.ping()
                delayTimer.reset()
                firstRun = false
            }
        }
    }

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
    }

    //Tells if the delay timer has expired or not
    fun hasDelayExpired(): Boolean {
        return delayTimer.milliseconds() >= readingDelayMs
    }
}