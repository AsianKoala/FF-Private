package robotuprising.koawalib.util

interface Invertable<T : Invertable<T>?> {
    /** Set the inversion
     *
     * @param invert Inversion to set
     * @return this
     */
    fun setInverted(invert: Boolean): T

    /** Invert
     *
     * @return this
     */
    fun invert(): T {
        return setInverted(!inverted)
    }

    /** Get current inversion
     *
     * @return Current inversion
     */
    val inverted: Boolean
}
