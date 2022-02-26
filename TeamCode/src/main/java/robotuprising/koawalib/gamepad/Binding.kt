package robotuprising.koawalib.gamepad

import robotuprising.koawalib.util.interfaces.KBoolean

interface Binding<T : KBoolean> : KBoolean {
    /** Button type
     *
     */
    enum class Type {
        NONE_ACTIVE, SOME_ACTIVE, ALL_ACTIVE
    }


    fun getSuppliers(): Array<T>
    fun getDefaultType(): Type


    override fun invokeBoolean(): Boolean {
        return get(getDefaultType())
    }

    /** Get this as boolean for the type
     *
     * @param type The type to get boolean as
     * @return If the binding meets the criteria
     */
    operator fun get(type: Type = getDefaultType()): Boolean {
        var on = false
        var off = getSuppliers().isEmpty()
        for (s in getSuppliers()) {
            if (s.invokeBoolean()) {
                on = true
            } else {
                off = true
            }
        }
        return when (type) {
            Type.NONE_ACTIVE -> !on
            Type.ALL_ACTIVE -> !off
            else -> on
        }
    }
}
