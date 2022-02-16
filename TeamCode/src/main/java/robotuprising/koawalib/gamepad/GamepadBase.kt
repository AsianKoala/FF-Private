package robotuprising.koawalib.gamepad

import com.qualcomm.robotcore.hardware.Gamepad
import robotuprising.koawalib.util.Enableable
import robotuprising.koawalib.util.Extensions.d
import robotuprising.koawalib.util.KBoolean
import robotuprising.koawalib.util.KDouble
import robotuprising.koawalib.util.Periodic


open class GamepadBase<T : ButtonBase, U : AxisBase>(
        private val gamepad: Gamepad,
        private val buttonClass: Class<T>,
        private val axisClass: Class<U>) : Periodic, Enableable<GamepadBase<T, U>> {
    private var enabled = true

    val a: T
    val b: T
    val x: T
    val y: T

    val start: T
    val back: T

    val leftBumper: T
    val rightBumper: T

    val dpadUp: T
    val dpadDown: T 
    val dpadLeft: T 
    val dpadRight: T 

    val leftStickButton: T
    val rightStickButton: T


    val leftTrigger: U
    val rightTrigger: U
    val leftStickX: U
    val leftStickY: U
    val rightStickX: U
    val rightStickY: U


    val leftStick: GamepadStick<U, T>
    val rightStick: GamepadStick<U, T>

    val dpad: GamepadDpad<T>

    private val periodics: Array<Periodic>
    private val enableables: Array<Enableable<*>>


    init {
        // set components
        // buttons
        a = buttonInstance { gamepad.a }
        b = buttonInstance { gamepad.b }
        x = buttonInstance { gamepad.x }
        y = buttonInstance { gamepad.y }

        start = buttonInstance { gamepad.start }
        back = buttonInstance { gamepad.back }

        // bumpers
        leftBumper = buttonInstance { gamepad.left_bumper }
        rightBumper = buttonInstance { gamepad.right_bumper }

        // dpad
        dpadUp = buttonInstance { gamepad.dpad_up }
        dpadDown = buttonInstance { gamepad.dpad_down }
        dpadLeft = buttonInstance { gamepad.dpad_left }
        dpadRight = buttonInstance { gamepad.dpad_right }

        // left stick
        leftStickX = axisInstance { gamepad.left_stick_x.d }
        leftStickY = axisInstance { gamepad.left_stick_y.d }
        leftStickButton = buttonInstance { gamepad.left_stick_button }

        // right stick
        rightStickX = axisInstance { gamepad.right_stick_x.d }
        rightStickY = axisInstance { gamepad.right_stick_y.d }
        rightStickButton = buttonInstance { gamepad.right_stick_button }

        // triggers
        leftTrigger = axisInstance { gamepad.left_trigger.d }
        rightTrigger = axisInstance { gamepad.right_trigger.d }


        // component groups
        // sticks
        leftStick = GamepadStick(leftStickX, leftStickY, leftStickButton)
        rightStick = GamepadStick(rightStickX, rightStickY, rightStickButton)

        // dpad
        dpad = GamepadDpad(dpadUp, dpadDown, dpadLeft, dpadRight)

        // arrays
        periodics = arrayOf(a, b, x, y, start, back, leftBumper, rightBumper,
                leftTrigger, rightTrigger, leftStick, rightStick, dpad)
        enableables = arrayOf(a, b, x, y, start, back, leftBumper, rightBumper,
                leftTrigger, rightTrigger, leftStick, rightStick, dpad)
    }

    enum class Button {
        A, B, X, Y, START, BACK, LEFT_BUMPER, RIGHT_BUMPER, LEFT_STICK_BUTTON, RIGHT_STICK_BUTTON
    }

    enum class Axis {
        LEFT_STICK_X, LEFT_STICK_Y, RIGHT_STICK_X, RIGHT_STICK_Y, LEFT_TRIGGER, RIGHT_TRIGGER
    }

    fun getButton(bu: Button): T {
        return when(bu) {
            Button.A -> a
            Button.B -> b
            Button.X -> x
            Button.Y -> y
            Button.START -> start
            Button.BACK -> back
            Button.LEFT_BUMPER -> leftBumper
            Button.RIGHT_BUMPER -> rightBumper
            Button.LEFT_STICK_BUTTON -> leftStickButton
            Button.RIGHT_STICK_BUTTON -> rightStickButton
        }
    }

    fun getAxis(ax: Axis): U {
        return when(ax) {
            Axis.LEFT_STICK_X -> leftStickX
            Axis.LEFT_STICK_Y -> leftStickY
            Axis.RIGHT_STICK_X -> rightStickX
            Axis.RIGHT_STICK_Y -> rightStickY
            Axis.LEFT_TRIGGER -> leftTrigger
            Axis.RIGHT_TRIGGER -> rightTrigger
        }
    }

    fun getButtonAsBoolean(bu: Button): Boolean {
        return getButton(bu).invokeBoolean()
    }

    fun getAxisAsDouble(ax: Axis): Double {
        return getAxis(ax).invokeDouble()
    }

    fun getAxisAsBoolean(ax: Axis): Boolean {
        return getAxis(ax).invokeBoolean()
    }

    private fun buttonInstance(b: KBoolean): T {
        return buttonClass.getConstructor(KBoolean::class.java).newInstance(b)
    }

    private fun axisInstance(d: KDouble): U {
        return axisClass.getConstructor(KDouble::class.java).newInstance(d)
    }

    fun getGamepad(): Gamepad {
        return gamepad
    }

    override fun periodic() {
        if(isDisabled) return
        periodics.forEach(Periodic::periodic)
    }

    override fun setEnabled(enable: Boolean): GamepadBase<T, U> {
        enabled = enable
        enableables.forEach { it.setEnabled(enable) }
        return this
    }

    override val isEnabled: Boolean get() = enabled

}