package robotuprising.koawalib.command.commands

import java.util.function.DoubleSupplier

class WaitCommand(private val supplier: DoubleSupplier) : Command {
    constructor(seconds: Double) : this({ seconds })

    val seconds get() = supplier.asDouble

    override fun execute() {

    }

    override fun isFinished() = supplier.asDouble <= getRuntime().seconds()
}
