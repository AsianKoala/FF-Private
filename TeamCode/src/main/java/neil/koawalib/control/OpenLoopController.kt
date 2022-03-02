package neil.koawalib.control

class OpenLoopController : Controller() {
    fun setDirectOutput(power: Double) {
        output = power
    }

    override fun process(): Double {
        return Double.NaN
    }
}