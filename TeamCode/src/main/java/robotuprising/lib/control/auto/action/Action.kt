package robotuprising.lib.control.auto.action

interface Action {
    fun start()
    fun update()
    fun isFinished(): Boolean
    fun stop()
}