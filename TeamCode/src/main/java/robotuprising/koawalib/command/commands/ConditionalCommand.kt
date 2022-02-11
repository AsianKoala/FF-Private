package robotuprising.koawalib.command.commands

import robotuprising.koawalib.command.scheduler.CommandScheduler
import java.util.function.BooleanSupplier

class ConditionalCommand(
    private val supplier: BooleanSupplier,
    private val trueCommand: Command? = null,
    private val falseCommand: Command? = null
) : Command {
    override fun execute() {

    }

    override fun isFinished(): Boolean {
        return when {
            trueCommand == null -> supplier.asBoolean
            falseCommand == null -> trueCommand.justFinished
            else -> trueCommand.justFinished || falseCommand.justFinished
        }
    }

    init {
        if(trueCommand != null) {
            CommandScheduler.scheduleWithOther(this, trueCommand, supplier)
        }

        if(falseCommand != null) {
            CommandScheduler.scheduleWithOther(this, falseCommand) { !supplier.asBoolean }
        }
    }
}