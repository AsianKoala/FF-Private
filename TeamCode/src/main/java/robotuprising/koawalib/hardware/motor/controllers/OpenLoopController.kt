package robotuprising.koawalib.hardware.motor.controllers

class OpenLoopController : Controller() {

    fun setDirectOutput(power: Double) {
        output = power
    }

    override fun process(): Double {
        return Double.NaN
    }

    override fun update() {
        if(disabled) {
            setDirectOutput(0.0)
        }
    }
}