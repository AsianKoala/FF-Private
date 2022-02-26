package robotuprising.koawalib.hardware.motor

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range
import robotuprising.koawalib.hardware.HardwareDevice
import robotuprising.koawalib.math.MathUtil.clip
import robotuprising.koawalib.math.MathUtil.d
import robotuprising.koawalib.util.KDouble
import robotuprising.lib.math.MathUtil.epsilonNotEqual
import kotlin.math.absoluteValue

open class KMotor: HardwareDevice<DcMotorEx>, KDouble {
    constructor(device: DcMotorEx) : super(device)
    constructor(name: String) : super(name)

    private var min = -1.0
    private var max = 1.0

    private var offset = 0

    fun setLimits(mi: Double, ma: Double): KMotor {
        min = min.clip(1.0)
        max = max.clip(1.0)
        return this
    }

    fun zero() {
        offset = device.currentPosition
    }

    open fun setSpeed(speed: Double) {
        power = speed
    }

    var power: Double = 0.0
        private set(value) {
            val clipped = Range.clip(value, min, max)
            if(clipped epsilonNotEqual field && (clipped == 0.0 ||clipped.absoluteValue == 1.0 || (clipped - field).absoluteValue > 0.005)) {
                field = value
                device.power = value
            }
        }

    var zeroPowerBehavior: DcMotor.ZeroPowerBehavior
        set(value) {
            if(device.zeroPowerBehavior != value) {
                device.zeroPowerBehavior = value
                field = value
            }
        }

    var direction: DcMotorSimple.Direction
        set(value) {
            if(device.direction != value) {
                device.direction = value
                field = value
            }
        }

    val position get() = (device.currentPosition - offset).d

    val velocity get() = device.velocity

    val brake: KMotor
        get() {
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            return this
        }

    val float: KMotor
        get() {
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            return this
        }

    val forward: KMotor
        get() {
            direction = DcMotorSimple.Direction.FORWARD
            return this
        }

    val reverse: KMotor
        get() {
            direction = DcMotorSimple.Direction.REVERSE
            return this
        }

    override fun invokeDouble(): Double {
        return power
    }

    init {
        device.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        direction = DcMotorSimple.Direction.FORWARD
    }
}