package org.firstinspires.ftc.teamcode.control.system

/**
 * state machines are glorified when statements anyway /s
 */
open class StateMachine(private val states: ArrayList<State>) {
    fun run() {
        for (state in states) {
            state.update()
        }
    }
}
