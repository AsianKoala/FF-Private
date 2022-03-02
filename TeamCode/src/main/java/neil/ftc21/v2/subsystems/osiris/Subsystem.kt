package neil.ftc21.v2.subsystems.osiris

abstract class Subsystem {
    // because subsystems are objects, they retain data between opmode stops/starts
    // to prevent this from interfering with different opmode runs, fun stop()
    // resets the subsystem to a "stopped" state
    abstract fun stop()

    abstract fun updateDashboard(debugging: Boolean)
}