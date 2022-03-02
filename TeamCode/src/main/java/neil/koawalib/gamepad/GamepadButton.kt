package neil.koawalib.gamepad

import neil.koawalib.gamepad.functionality.Input

class GamepadButton(private val button: () -> Boolean) : ButtonBase(), Input<GamepadButton> {
    override fun instance(): GamepadButton {
        return this
    }

    override fun invokeBoolean(): Boolean {
        return button.invoke()
    }
}
