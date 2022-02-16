package robotuprising.koawalib.gamepad

import robotuprising.koawalib.util.Enableable
import robotuprising.koawalib.util.Invertable
import robotuprising.koawalib.util.KBoolean
import robotuprising.koawalib.util.Periodic


open class ButtonBase(protected var booleanSupplier: (() -> Boolean)?) : KBoolean, Periodic, Invertable<ButtonBase>, Enableable<ButtonBase> {
    /** Returns if the button is pressed
     *
     * @return The above condition
     */
    var isPressed = false
        private set

    /** Returns if the button is toggled
     *
     * @return The above condition
     */
    var isToggled = false
        private set
    private var recentAction = false
    private var pastState = false
    override var inverted = false
    override var isEnabled = true

    override fun periodic() {
        periodic(invokeBoolean())
    }

    private fun periodic(currentState: Boolean) {
        if (isDisabled) {
            recentAction = false
            pastState = false
            isPressed = false
            isToggled = false
            return
        }
        recentAction = pastState != currentState
        pastState = currentState
        isPressed = currentState
        isToggled = (recentAction && pastState) != isToggled
    }

    /** Returns if the button is just pressed
     *
     * @return The above condition
     */
    val isJustPressed: Boolean
        get() = isPressed && recentAction

    /** Returns if the button is just released
     *
     * @return The above condition
     */
    val isJustReleased: Boolean
        get() = !isPressed && recentAction

    /** Returns if the button is released
     *
     * @return The above condition
     */
    val isReleased: Boolean
        get() = !isPressed

    /** Returns if the button is just toggled
     *
     * @return The above condition
     */
    val isJustToggled: Boolean
        get() = isToggled && recentAction && isPressed

    /** Returns if the button is just untoggled
     *
     * @return The above condition
     */
    val isJustInverseToggled: Boolean
        get() = !isToggled && recentAction && isPressed

    /** Returns if the button is untoggled
     *
     * @return The above condition
     */
    val isInverseToggled: Boolean
        get() = !isToggled

    override fun invokeBoolean(): Boolean {
        return this.booleanSupplier!!.invoke() xor inverted && isEnabled
    }

    override fun setInverted(invert: Boolean): ButtonBase {
        inverted = invert
        return this
    }

    override fun setEnabled(enable: Boolean): ButtonBase {
        isEnabled = enable
        return this
    }
}
