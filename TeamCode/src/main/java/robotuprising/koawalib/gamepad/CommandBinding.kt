package robotuprising.koawalib.gamepad

class CommandBinding(
        private val defaltType: Binding.Type,
        private vararg val inputs: CommandInput<*>)
    : CommandButton(null), Binding<CommandInput<*>> {
    constructor(vararg b: CommandInput<*>) : this(Binding.Type.ALL_ACTIVE, *b)

    override fun getSuppliers(): Array<CommandInput<*>> {
        return arrayOf(*inputs)
    }

    override fun getDefaultType(): Binding.Type {
        return defaltType
    }

    override fun invokeBoolean(): Boolean {
        return get()
    }
}
