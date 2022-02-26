package robotuprising.lib.system.statemachine.transition

import robotuprising.koawalib.statemachine.transition.TransitionCondition

class TimedTransition(val time: Double) : TransitionCondition {
    private var startTime = 0L

    fun startTimer() {
        startTime = System.nanoTime()
    }

    override fun shouldTransition(): Boolean {
        return (System.nanoTime() - startTime) / 1e9 > time
    }
}
