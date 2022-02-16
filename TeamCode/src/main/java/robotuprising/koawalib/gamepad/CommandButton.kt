package robotuprising.koawalib.gamepad

import robotuprising.koawalib.command.commands.Command

open class CommandButton(supplier: (() -> Boolean)?) : ButtonBase(supplier), CommandInput<CommandButton> {
    override fun instance(): CommandButton {
        return this
    }

    fun schedule(f: (Boolean) -> Command): CommandButton {
        return schedule(f.invoke(invokeBoolean()))
    }

    @JvmName("bruh1")
    fun schedule(f: (Boolean) -> Unit): CommandButton {
        return schedule(Command { f.invoke(invokeBoolean()) })
    }

    override fun setInverted(invert: Boolean): CommandButton {
        return super.setInverted(invert) as CommandButton
    }
}
