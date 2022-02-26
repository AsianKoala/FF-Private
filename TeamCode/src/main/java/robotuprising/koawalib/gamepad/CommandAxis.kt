package robotuprising.koawalib.gamepad

import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.util.interfaces.KDouble

class CommandAxis(supplier: () -> Double, threshold: Double = DEFAULT_TRIGGER_THRESHOLD) : AxisBase(supplier, threshold), CommandInput<CommandAxis> {
    override fun instance(): CommandAxis {
        return this
    }

    override fun setTriggerThreshold(threshold: Double): CommandAxis {
        super.setTriggerThreshold(threshold)
        return this
    }

    fun schedulePressed(f: (KDouble) -> Command): CommandAxis {
        return whilePressed(f.invoke(this::invokeDouble))
    }

    fun schedule(f: (Double) -> Command): CommandAxis {
        return schedule(f.invoke(invokeDouble()))
    }

    override fun setInverted(invert: Boolean): CommandAxis {
        return super.setInverted(invert) as CommandAxis
    }

    fun getAsButton(): CommandButton {
        return CommandButton(this::invokeBoolean)
    }

    fun getAsButton(threshold: Double): CommandButton {
        return CommandButton { if (threshold >= 0) invokeDouble() >= threshold else invokeDouble() < threshold }
    }

}
