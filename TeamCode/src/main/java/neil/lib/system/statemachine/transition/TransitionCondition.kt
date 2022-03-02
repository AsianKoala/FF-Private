package neil.lib.system.statemachine.transition

fun interface TransitionCondition {
    fun shouldTransition(): Boolean
}
