package robotuprising.koawalib.util

interface Enableable<T : Enableable<T>?> {
    fun enable(): T {
        return setEnabled(true)
    }

    fun disable(): T {
        return setEnabled(false)
    }

    fun setEnabled(enable: Boolean): T
    fun toggleEnabled(): T {
        return setEnabled(!isEnabled)
    }

    val isEnabled: Boolean
    val isDisabled: Boolean
        get() = !isEnabled
}
