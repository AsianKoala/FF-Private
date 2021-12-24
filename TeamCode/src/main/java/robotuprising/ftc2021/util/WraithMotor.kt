package robotuprising.ftc2021.util

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.openftc.revextensions2.ExpansionHubEx
import org.openftc.revextensions2.ExpansionHubMotor
import robotuprising.lib.math.MathUtil.epsilonNotEqual
import robotuprising.lib.opmode.WraithDashboard
import kotlin.math.absoluteValue

// todo fix bulk position
// todo make everything else read from bulk reads
class WraithMotor(private val name: String, private val onMaster: Boolean) {
    private val motor = BulkDataManager.hwMap[ExpansionHubMotor::class.java, name]

    var power: Double = 0.0
        set(value) {
            val clipped = Range.clip(value, -1.0, 1.0)
            if (clipped epsilonNotEqual field && (clipped == 0.0 || clipped.absoluteValue == 1.0 || (clipped - field).absoluteValue > 0.005)) {
                field = value
                motor.power = value
            }
        }

    val bulkPosition: Int
        get() {
            return if (onMaster) {
                BulkDataManager.masterData.getMotorCurrentPosition(Globals.MASTER_MAPPINGS.indexOf(name))
            } else {
                BulkDataManager.slaveData.getMotorCurrentPosition(Globals.SLAVE_MAPPINGS.indexOf(name))
            }
        }

    val position: Int get() = motor.currentPosition

    val velocity: Double get() = motor.velocity

    val reverse: WraithMotor
        get() {
            direction = DcMotorSimple.Direction.REVERSE
            return this
        }

    val forward: WraithMotor
        get() {
            direction = DcMotorSimple.Direction.FORWARD
            return this
        }

    val float: WraithMotor
        get() {
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            return this
        }

    val brake: WraithMotor
        get() {
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            return this
        }

    val openLoopControl: WraithMotor
        get() {
            mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            return this
        }

    val velocityControl: WraithMotor
        get() {
            mode = DcMotor.RunMode.RUN_USING_ENCODER
            return this
        }

    val positionControl: WraithMotor
        get() {
            mode = DcMotor.RunMode.RUN_TO_POSITION
            return this
        }

    val resetEncoder: WraithMotor
        get() {
            mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            return this
        }

    var mode: DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        set(value) {
            if (field != value) {
                motor.mode = value
                field = value
            }
        }

    var zeroPowerBehavior = DcMotor.ZeroPowerBehavior.UNKNOWN
        set(value) {
            if (value != field) {
                if (value != DcMotor.ZeroPowerBehavior.UNKNOWN)
                    motor.zeroPowerBehavior = value
                field = value
            }
        }

    var direction: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD
        set(value) {
            if (value != field) {
                motor.direction = value
                field = value
            }
        }

    val hub: String
    val portNumber: Int
    val current get() = motor.getCurrent(CurrentUnit.MILLIAMPS)
    val currentDraw get() = motor.getCurrentDraw(ExpansionHubEx.CurrentDrawUnits.MILLIAMPS)
    val overTemp get() = motor.isBridgeOverTemp

    fun sendDataToDashboard() {
        WraithDashboard.addLine("$name motor data")
        WraithDashboard["name"] = name
        WraithDashboard["$name hub"] = hub
        WraithDashboard["$name port number"] = portNumber
        WraithDashboard["$name mode"] = mode
        WraithDashboard["$name direction"] = direction
        WraithDashboard["$name zero behavior"] = zeroPowerBehavior
        WraithDashboard["$name current"] = current
        WraithDashboard["$name current draw"] = currentDraw
        WraithDashboard["$name over temp"] = overTemp
    }

    init {
        if (onMaster) {
            hub = "master"
            portNumber = Globals.MASTER_MAPPINGS.indexOf(name)
        } else {
            hub = "slave"
            portNumber = Globals.SLAVE_MAPPINGS.indexOf(name)
        }
    }
}
