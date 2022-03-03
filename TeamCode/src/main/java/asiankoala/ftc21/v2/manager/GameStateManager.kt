package asiankoala.ftc21.v2.manager

import com.qualcomm.robotcore.util.ElapsedTime
import asiankoala.ftc21.v2.lib.util.Extensions.d

object GameStateManager {
    private val gameTimer = ElapsedTime()

    private val autoTime = 30.0
    private val transitionTime = 8.0 + autoTime
    private val teleopTime = 120.0 + transitionTime
    private val endgameTime = 30.0 + teleopTime

    fun startTimer() {
        gameTimer.reset()
    }

    private var lastCycleTime = -1

    val systemState: SystemStates get() = when {
        gameTimer.seconds() > endgameTime -> SystemStates.FINISHED
        gameTimer.seconds() > teleopTime -> SystemStates.ENDGAME
        gameTimer.seconds() > transitionTime -> SystemStates.TELEOP
        gameTimer.seconds() > autoTime -> SystemStates.TRANSITION
        gameTimer.seconds() > 0.0 -> SystemStates.AUTO
        else -> SystemStates.INIT
    }


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


