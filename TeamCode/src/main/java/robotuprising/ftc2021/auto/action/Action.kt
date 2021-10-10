package robotuprising.ftc2021.auto.action

interface Action {
    fun start()
    fun update()
    fun isFinished(): Boolean
    fun stop()
}