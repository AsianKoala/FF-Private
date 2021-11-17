package robotuprising.ftc2021.util

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range
import org.openftc.revextensions2.ExpansionHubMotor
import kotlin.math.absoluteValue

class NakiriMotor(name: String, private val onMaster: Boolean) {
    private val motor = Globals.hwMap[ExpansionHubMotor::class.java, name]

    var power: Double = 0.0
        set(value) {
            val clipped = Range.clip(value, -1.0, 1.0)
            if (clipped != field && (clipped == 0.0 || clipped.absoluteValue == 1.0 || (clipped - field).absoluteValue > 0.005)) {
                field = value
                motor.power = value
            }
        }

    var targetPosition: Int = 0
        set(value) {
            if (value != field) {
                field = value
            }
        }


    val position: Int
        get() {
            return if (onMaster) {
                BulkDataManager.masterData.getMotorCurrentPosition(motor)
            } else {
                BulkDataManager.slaveData.getMotorCurrentPosition(motor)
            }
        }


    val reverse: NakiriMotor
        get() {
            direction = DcMotorSimple.Direction.REVERSE
            return this
        }

    val forward: NakiriMotor
        get() {
            direction = DcMotorSimple.Direction.FORWARD
            return this
        }

    val float: NakiriMotor
        get() {
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            return this
        }

    val brake: NakiriMotor
        get() {
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            return this
        }

    val openLoopControl: NakiriMotor
        get() {
            mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            return this
        }

    val velocityControl: NakiriMotor
        get() {
            mode = DcMotor.RunMode.RUN_USING_ENCODER
            return this
        }

    val positionControl: NakiriMotor
        get() {
            mode = DcMotor.RunMode.RUN_TO_POSITION
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

}
