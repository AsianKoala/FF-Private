package robotuprising.koawalib.gamepad

class GamepadDpad<T : ButtonBase>(val up: T, val down: T, val left: T, val right: T) : Stick {
    private var enabled = false

    override fun periodic() {
        if(isDisabled) return;
        up.periodic();
        down.periodic();
        left.periodic();
        right.periodic();
    }

    override fun getXAxis(): Double {
        return if (right.invokeBoolean()) {
            if (left.invokeBoolean()) 0.0 else 1.0
        } else if(left.invokeBoolean()) -1.0
        else 0.0
    }

    override fun getYAxis(): Double {
        return if (up.invokeBoolean()) {
            if (down.invokeBoolean()) 0.0 else 1.0
        } else if(down.invokeBoolean()) -1.0
        else 0.0
    }

    override fun setEnabled(enable: Boolean): GamepadDpad<T> {
        enabled = enable;
        up.setEnabled(enabled);
        down.setEnabled(enabled);
        left.setEnabled(enabled);
        right.setEnabled(enabled);
        return this;
    }

    override val isEnabled: Boolean get() = enabled
}