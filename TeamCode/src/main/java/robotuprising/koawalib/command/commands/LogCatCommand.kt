package robotuprising.koawalib.command.commands

import android.util.Log

class LogCatCommand(
        tag: String = "KOAWALIB",
        message: String = "",
        priority: Int = Log.DEBUG
) : InstantCommand({ Log.println(priority, tag, message) }) {
    override val runsWhenDisabled: Boolean = true
}