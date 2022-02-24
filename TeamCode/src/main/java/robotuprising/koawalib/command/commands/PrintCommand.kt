package robotuprising.koawalib.command.commands

// print a messag to console
class PrintCommand(message: Any?) : InstantCommand({ println(message) })