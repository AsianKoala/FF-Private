package robotuprising.koawalib.gamepad

class GamepadStick<T : AxisBase, U : ButtonBase>(val xAxis: T, val yAxis: T, val stickButton: U) : Stick {
    private var enabled = true

    override fun periodic() {
        if(isDisabled) return
        xAxis.periodic()
        yAxis.periodic()
        stickButton.periodic()
    }

    override fun getXAxis(): Double {
        return xAxis.invokeDouble()
    }

    override fun getYAxis(): Double {
        return yAxis.invokeDouble()
    }

    override fun setEnabled(enable: Boolean): GamepadStick<T, U> {
        enabled = enable
        xAxis.setEnabled(enable)
        yAxis.setEnabled(enable)
        stickButton.setEnabled(enable)
        return this
    }

    override val isEnabled: Boolean get() = enabled
}
