package robotuprising.koawalib.util

interface Invertable<T : Invertable<T>?> {
    fun setInverted(invert: Boolean): T

    fun invert(): T {
        return setInverted(!inverted)
    }

    val inverted: Boolean
}
