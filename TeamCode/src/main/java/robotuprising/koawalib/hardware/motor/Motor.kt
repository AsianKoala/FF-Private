package robotuprising.koawalib.hardware.motor

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range
import robotuprising.koawalib.hardware.HardwareDevice
import robotuprising.koawalib.util.Extensions.clip
import robotuprising.koawalib.util.Invertable
import robotuprising.koawalib.util.KDouble
import robotuprising.lib.math.MathUtil.epsilonNotEqual
import kotlin.math.absoluteValue

open class Motor<T : DcMotorEx> : HardwareDevice<T>, Invertable<Motor<T>>, KDouble {
    constructor(device: T) : super(device)
    constructor(name: String) : super(name)

    private var invert = false
    private var min = -1.0
    private var max = 1.0

    private var offset = 0

    fun setLimits(mi: Double, ma: Double): Motor<T> {
        min = min.clip(1.0)
        max = max.clip(1.0)
        return this
    }

    fun zero() {
        offset = device.currentPosition
    }

    override val inverted: Boolean
        get() = invert

    override fun setInverted(invert: Boolean): Motor<T> {
        device.direction = if(invert) DcMotorSimple.Direction.REVERSE else DcMotorSimple.Direction.FORWARD
        this.invert = invert
        return this
    }

    override fun invert(): Motor<T> {
        setInverted(!invert)
        return this
    }

    var power: Double = 0.0
        set(value) {
            val clipped = Range.clip(value, min, max)
            if(clipped epsilonNotEqual field && (clipped == 0.0 ||clipped.absoluteValue == 1.0 || (clipped - field).absoluteValue > 0.005)) {
                field = value
                device.power = value
            }
        }

    val position: Int get() = device.currentPosition - offset

    val velocity: Double get() = device.velocity

    var mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        set(value) {
            if(field != value) {
                field = value
            }
        }

    override fun invokeDouble(): Double {
        return power
    }
}