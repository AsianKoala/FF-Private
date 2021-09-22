package org.firstinspires.ftc.teamcode.control.system

abstract class State {
    abstract fun run()
    abstract fun onStart()
    abstract fun onKill()

    abstract val name: String
    abstract val killCond: Boolean
    abstract val runCond: Boolean

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
