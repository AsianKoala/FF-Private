package neil.ftc21.v2.auto.pp

class Subroutines {

    interface Subroutine

    fun interface OnceOffSubroutine : Subroutine {
        fun runOnce()
    }

    fun interface RepeatedSubroutine: Subroutine {
        fun runLoop(): Boolean // return whether advance
    }

    // Once a position is reached, if that position has an ArrivalInterruptSubroutine, we advance to
    // the next waypoint (so the waypoint with the ArrivalInterruptSubroutine is our current waypoint)
    // and we call runCycle every tick until it eventually returns true
    fun interface ArrivalInterruptSubroutine: Subroutine {
        fun runCycle(): Boolean // return whether complete
    }
}