package robotuprising.ftc2021.util

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.openftc.revextensions2.ExpansionHubMotor

object NakiriMotorFactory {
    private lateinit var hwMap: HardwareMap
    fun linkHardwareMap(hardwareMap: HardwareMap) {
        hwMap = hardwareMap
    }

    private var name: String = ""
    private var onMaster: Boolean = false

    private var motor = hwMap[ExpansionHubMotor::class.java, name]

    private var mode: DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        set(value) {
            if (field != value) {
                motor.mode = value
                field = value
            }
        }

    private var zeroPowerBehavior = DcMotor.ZeroPowerBehavior.UNKNOWN
        set(value) {
            if (value != field) {
                if (value != DcMotor.ZeroPowerBehavior.UNKNOWN)
                    motor.zeroPowerBehavior = value
                field = value
            }
        }

    private var direction: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD
        set(value) {
            if (value != field) {
                motor.direction = value
                field = value
            }
        }

    fun name(str: String): NakiriMotorFactory {
        name = str
        motor = hwMap[ExpansionHubMotor::class.java, name]
        mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        direction = DcMotorSimple.Direction.FORWARD
        onMaster = false
        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        return this
    }

    val master: NakiriMotorFactory
        get() {
            onMaster = true
            return this
        }

    val slave: NakiriMotorFactory
        get() {
            onMaster = false
            return this
        }

    val reverse: NakiriMotorFactory
        get() {
            direction = DcMotorSimple.Direction.REVERSE
            return this
        }

    val forward: NakiriMotorFactory
        get() {
            direction = DcMotorSimple.Direction.FORWARD
            return this
        }

    val float: NakiriMotorFactory
        get() {
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            return this
        }

    val brake: NakiriMotorFactory
        get() {
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            return this
        }

    val openLoopControl: NakiriMotorFactory
        get() {
            mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            return this
        }

    val velocityControl: NakiriMotorFactory
        get() {
            mode = DcMotor.RunMode.RUN_USING_ENCODER
            return this
        }

    val positionControl: NakiriMotorFactory
        get() {
            mode = DcMotor.RunMode.RUN_TO_POSITION
            return this
        }

    val resetEncoder: NakiriMotorFactory
        get() {
            mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            return this
        }

    val create: NakiriMotor
        get() {
            return NakiriMotor(name, onMaster, motor)
        }
}
