package asiankoala.junk.v2.lib.system.statemachine.transition

fun interface TransitionCondition {
    fun shouldTransition(): Boolean
}
