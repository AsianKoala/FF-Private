package robotuprising.koawalib.command.commands

enum class CommandState {
    RESET,
    STARTED,
    INITIALIZING,
    EXECUTING,
    FINISHED,
    INTERRUPTING,
    CANCELLED
}