package neil.koawalib.gamepad

import neil.koawalib.gamepad.functionality.Stick

class GamepadStick(
        private val xGamepadAxis: GamepadAxis,
        private val yGamepadAxis: GamepadAxis,
        private val stickButton: GamepadButton
) : Stick {
    override fun periodic() {
        xGamepadAxis.periodic()
        yGamepadAxis.periodic()
        stickButton.periodic()
    }

    override val xAxis
        get() = xGamepadAxis.invokeDouble()

    override val yAxis
        get() = yGamepadAxis.invokeDouble()
}
