package robotuprising.koawalib.subsystem.motor.controllers

abstract class Controller {
    protected abstract fun process(): Double

    var disabled: Boolean = false

    var output = 0.0

    fun enable() {
        disabled = false
    }

    fun disable() {
        disabled = true
    }

    open fun update() {
        output = if(disabled) {
            0.0
        } else {
            process()
        }
    }
}