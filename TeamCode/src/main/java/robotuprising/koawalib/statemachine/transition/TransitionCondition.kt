package robotuprising.koawalib.statemachine.transition

fun interface TransitionCondition {
    fun shouldTransition(): Boolean
}
