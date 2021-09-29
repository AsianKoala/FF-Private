package robotuprising.ftc2021.control.action

interface Action {
    fun start()
    fun update()
    fun isFinished(): Boolean
    fun stop()
}