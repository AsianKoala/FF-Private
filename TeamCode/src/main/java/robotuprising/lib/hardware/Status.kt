package robotuprising.lib.hardware

enum class Status {
    INIT,
    RUNNING,
    DEAD;

    val shouldUpdate
        get() = this == RUNNING
}