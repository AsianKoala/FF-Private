package robotuprising.koawalib.util

enum class OpModeState {
    INIT, INIT_LOOP, LOOP, STOP;

    fun isStatus(vararg states: OpModeState): Boolean {
        states.forEach {
            if(this == it) return true
        }
        return false
    }
}
