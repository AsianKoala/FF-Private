package robotuprising.lib.system

/**
 * state machines are glorified when statements anyway /s
 */
open class StateMachine(private val states: ArrayList<State>) {

    var active = true
        private set

    var killcount = 0

    fun run() {
        for (state in states) {
            state.update()
            if(state.killed)
                killcount++
        }
        if(killcount==states.size)
            active = false
        killcount = 0
    }
}
