package robotuprising.ftc2021.manager

import robotuprising.lib.util.Extensions.d

object GameStateManager {
    private var lastCycleTime = -1

    var systemState = SystemStates.INIT

    var gameState = GameStates.IDLE
        set(value) {
            when {
                value == GameStates.MOVING_BACK && lastCycleTime == -1 ->
                    lastCycleTime = System.currentTimeMillis().toInt()

                value == GameStates.MOVING_BACK && lastCycleTime != -1 -> {
                    val cycleTime = System.currentTimeMillis() - lastCycleTime
                    cycleTimes.add(cycleTime.d / 1000)
                    lastCycleTime = System.currentTimeMillis().toInt()
                }
            }

            field = value
        }


    private val cycleTimes = ArrayList<Double>()
}


