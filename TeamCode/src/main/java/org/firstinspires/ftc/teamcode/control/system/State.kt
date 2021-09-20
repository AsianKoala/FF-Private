package org.firstinspires.ftc.teamcode.control.system

abstract class State {
    protected abstract fun run()

    open val name: String = ""
    open val killCond: Boolean = true
    open val runCond: Boolean = true

    open fun onStart() {}
    open fun onKill() {}

    var started: Boolean = false
        private set
    var killed: Boolean = false
        private set

    fun update() {
        if (!started && runCond) {
            onStart()
            started = true
        }

        if (!killed) {
            if (runCond)
                run()

            if (killCond) {
                onKill()
                killed = true
            }
        }
    }
}
