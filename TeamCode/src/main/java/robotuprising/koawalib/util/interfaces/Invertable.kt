package robotuprising.koawalib.util.interfaces

interface Invertable<T : Invertable<T>?> {
    fun setInverted(invert: Boolean): T

    fun invert(): T {
        return setInverted(!inverted)
    }

    val inverted: Boolean
}
