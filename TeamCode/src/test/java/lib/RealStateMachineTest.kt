package lib

import robotuprising.lib.system.State
import robotuprising.lib.system.StateMachineBuilder

object RealStateMachineTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val start = System.currentTimeMillis()
        val sm = StateMachineBuilder()
            .addState(object : State() {
                override fun run() {
                    println("$name running")
                }

                override fun onStart() {
                    println("started")
                }

                override fun onKill() {
                    println("killed")
                }

                override val name: String = "first state"
                override val killCond: Boolean = true
                override val runCond: Boolean = true
            })
            .addState(object : State() {
                override fun run() {
                    println("$name is running")
                }

                override fun onStart() {
                    println("$name started")
                }

                override fun onKill() {
                    println("$name killed")
                }

                override val name: String = "time based state"
                override val killCond: Boolean
                    get() = System.currentTimeMillis() - start > 3050
                override val runCond: Boolean
                    get() = System.currentTimeMillis() - start > 3000
            })
            .build()

        while (sm.active) {
            sm.run()
        }
    }
}
