package asiankoala.ftc21.v2.lib.system.statemachine.transition

fun interface TransitionCondition {
    fun shouldTransition(): Boolean
}
