package robotuprising.lib.util.hardware

enum class Status {
    INIT,
    RUNNING,
    STALLING,
    STOPPED,
    DEAD;

    val shouldUpdate
        get() = INIT.ordinal < ordinal && ordinal < STOPPED.ordinal
}