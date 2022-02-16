package robotuprising.koawalib.hardware.motor

import com.qualcomm.robotcore.hardware.DcMotorSimple
import robotuprising.koawalib.hardware.HardwareDevice
import robotuprising.koawalib.util.Extensions.clip
import robotuprising.koawalib.util.Invertable
import robotuprising.koawalib.util.KDouble

class Motor<T : DcMotorSimple> : HardwareDevice<T>, Invertable<Motor<T>>, KDouble {
    constructor(device: T) : super(device)
    constructor(name: String) : super(name)

    private var min = -1.0
    private var max = 1.0

    fun setLimits(mi: Double, ma: Double): Motor<T> {
        min = min.clip(1.0)
        max = max.clip(1.0)
        return this
    }

    override fun setInverted(invert: Boolean): Motor<T> {
        TODO("Not yet implemented")
    }

    override val inverted: Boolean
        get() = TODO("Not yet implemented")

    override fun invokeDouble(): Double {
        TODO("Not yet implemented")
    }
}