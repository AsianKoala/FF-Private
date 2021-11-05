package robotuprising.lib.system.statemachine.transition

import robotuprising.lib.util.Extensions.d

class TimedTransition(val time: Double): TransitionCondition {
    private var startTime = 0L

    fun startTimer() {
        startTime = System.nanoTime()
    }

    override fun shouldTransition(): Boolean {
        return (System.nanoTime() - startTime) / 1e9 > time
    }
}