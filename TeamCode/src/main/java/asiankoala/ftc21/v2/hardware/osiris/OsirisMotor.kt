package asiankoala.ftc21.v2.hardware.osiris

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range
import org.openftc.revextensions2.ExpansionHubMotor
import asiankoala.ftc21.v2.manager.BulkDataManager
import asiankoala.ftc21.v2.subsystems.osiris.motor.MotorConfig
import asiankoala.ftc21.v2.lib.math.MathUtil.epsilonNotEqual
import kotlin.math.absoluteValue

class OsirisMotor(private val name: String, private val onMaster: Boolean) {
    // optional constructor used for MotorSubsystems
    constructor(motorConfig: MotorConfig) : this(motorConfig.name, false) {
        direction = motorConfig.direction
        zeroPowerBehavior = motorConfig.zeroPowerBehavior
        mode = motorConfig.mode
    }

    private val motor by lazy { BulkDataManager.hwMap[ExpansionHubMotor::class.java, name] }

    private var zeroPowerChanged = false
    private var directionChanged = false
    private var modeChanged = false

    private fun hardwareUpdate() {
        if(zeroPowerChanged) {
            motor.zeroPowerBehavior = zeroPowerBehavior
//            OsirisDashboard.addLine("changed config of $name")
        }

        if(directionChanged) {
            motor.direction = direction
//            OsirisDashboard.addLine("changed config of $name")
        }

        if(modeChanged) {
            motor.mode = mode
//            OsirisDashboard.addLine("changed config of $name")
        }

        zeroPowerChanged = false
        directionChanged = false
        modeChanged = false
    }

    var power: Double = 0.0
        set(value) {
            val clipped = Range.clip(value, -1.0, 1.0)
            if (clipped epsilonNotEqual field && (clipped == 0.0 || clipped.absoluteValue == 1.0 || (clipped - field).absoluteValue > 0.005)) {
                hardwareUpdate()
                field = value
                motor.power = value
            }
//            OsirisDashboard.addLine("requested power $field on motor $name")
        }

    val position: Int get() {
        hardwareUpdate()
        return if(onMaster) {
            BulkDataManager.masterData.getMotorCurrentPosition(motor)
        } else {
            BulkDataManager.slaveData.getMotorCurrentPosition(motor)
        }
//        OsirisDashboard.addLine("requested position from motor $name")
//        return motor.currentPosition
    }

    val velocity: Double
        get() {
            hardwareUpdate()
            return motor.velocity
         }

    var mode: DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        set(value) {
            if (field != value) {
                modeChanged = true
                field = value
            }
        }

    var zeroPowerBehavior = DcMotor.ZeroPowerBehavior.UNKNOWN
        set(value) {
            if (value != field) {
                if (value != DcMotor.ZeroPowerBehavior.UNKNOWN)
                    zeroPowerChanged = true
                field = value
            }
        }

    var direction: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD
        set(value) {
            if (value != field) {
                directionChanged = true
                field = value
            }
        }

    val reverse: OsirisMotor
        get() {
            direction = DcMotorSimple.Direction.REVERSE
            return this
        }

    val forward: OsirisMotor
        get() {
            direction = DcMotorSimple.Direction.FORWARD
            return this
        }

    val float: OsirisMotor
        get() {
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            return this
        }

    val brake: OsirisMotor
        get() {
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            return this
        }

    val openLoopControl: OsirisMotor
        get() {
            mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            return this
        }

    val velocityControl: OsirisMotor
        get() {
            mode = DcMotor.RunMode.RUN_USING_ENCODER
            return this
        }

    val positionControl: OsirisMotor
        get() {
            mode = DcMotor.RunMode.RUN_TO_POSITION
            return this
        }

    val resetEncoder: OsirisMotor
        get() {
            mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            return this
        }



//    val hub: String
//    val portNumber: Int
//    val current get() = motor.getCurrent(CurrentUnit.MILLIAMPS)
//    val currentDraw get() = motor.getCurrentDraw(ExpansionHubEx.CurrentDrawUnits.MILLIAMPS)
//    val overTemp get() = motor.isBridgeOverTemp

//        fun sendDataToDashboard() {
//        OsirisDashboard.addLine("$name motor data")
//        OsirisDashboard["name"] = name
//        OsirisDashboard["$name hub"] = hub
//        OsirisDashboard["$name port number"] = portNumber
//        OsirisDashboard["$name mode"] = mode
//        OsirisDashboard["$name direction"] = direction
//        OsirisDashboard["$name zero behavior"] = zeroPowerBehavior
//        OsirisDashboard["$name current"] = current
//        OsirisDashboard["$name current draw"] = currentDraw
//        OsirisDashboard["$name over temp"] = overTemp
//        }

//        init {
//        if (onMaster) {
//            hub = "master"
//            portNumber = Globals.MASTER_MAPPINGS.indexOf(name)
//        } else {
//            hub = "slave"
//            portNumber = Globals.SLAVE_MAPPINGS.indexOf(name)
//        }
//        }
//    }
}
