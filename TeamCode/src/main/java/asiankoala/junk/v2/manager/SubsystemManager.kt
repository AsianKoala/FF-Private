package asiankoala.junk.v2.manager

import asiankoala.junk.v2.hardware.interfaces.*
import asiankoala.junk.v2.hardware.osiris.interfaces.*
import asiankoala.junk.v2.lib.opmode.OsirisDashboard
import asiankoala.junk.v2.subsystems.Subsystem
import asiankoala.junk.v2.subsystems.motor.ServoSubsystem

// todo if we make it to states: Create a fake hardware class (fake servo, fake motor) and in this class
// todo only if they are enabled, then send power/position to the real motor/subsystem
// todo if we do stuff like that, guarantee that we are creating a new motor every time new opmode
// todo -> shit like what happened at remote wont happen
object SubsystemManager {
    private val subsystems: ArrayList<Subsystem> = ArrayList()
    private val loopableSubsystems: ArrayList<Loopable> = ArrayList()
    private val zeroableSubsystems: ArrayList<Zeroable> = ArrayList()
    private val readableSubsystems: ArrayList<Readable> = ArrayList()
    private val testableSubsystems: ArrayList<Testable> = ArrayList()
    private val initializableSubsystems: ArrayList<Initializable> = ArrayList()
    private val startableSubsystems: ArrayList<Startable> = ArrayList()

    private val managerLists = listOf(
        subsystems, loopableSubsystems, zeroableSubsystems,
        readableSubsystems, testableSubsystems, initializableSubsystems, startableSubsystems
    )

    private var zeroedYet = false

    fun stopAll() {
        subsystems.forEach(Subsystem::stop)
    }

    fun updateDashboardAll(debugging: Boolean) {
        OsirisDashboard["subsystems length"] = subsystems.size
        OsirisDashboard.addSpace()
        subsystems.forEach { it.updateDashboard(debugging) }
    }

    fun loopAll() {
        loopableSubsystems.forEach(Loopable::loop)
    }

    fun readAll() {
        readableSubsystems.forEach(Readable::read)
    }

    fun testAll() {
        testableSubsystems.forEach(Testable::test)
    }

    fun initAll() {
        initializableSubsystems.forEach(Initializable::init)
    }

    fun startAll() {
        startableSubsystems.forEach(Startable::start)
    }

    fun initServos() {
        subsystems.forEach {
            if (it is ServoSubsystem) {
                it.moveServoToPosition(it.config.homePosition)
            }
        }
    }

    fun periodic() {
        readAll()
        loopAll()
        updateDashboardAll(false)
    }

    fun registerSubsystems(vararg allSubsystems: Subsystem) {
        allSubsystems.forEach { register(it) }
    }

    fun deregisterSubsystems(vararg allSubsystems: Subsystem) {
        allSubsystems.forEach { deregister(it) }
    }

    fun register(subsystem: Subsystem) {
        subsystems.add(subsystem)

        if (subsystem is Loopable) {
            loopableSubsystems.add(subsystem)
        }

        if (subsystem is Zeroable) {
            zeroableSubsystems.add(subsystem)
        }

        if (subsystem is Readable) {
            readableSubsystems.add(subsystem)
        }

        if (subsystem is Testable) {
            testableSubsystems.add(subsystem)
        }

        if (subsystem is Initializable) {
            initializableSubsystems.add(subsystem)
        }

        if (subsystem is Startable) {
            startableSubsystems.add(subsystem)
        }
    }

    fun deregister(subsystem: Subsystem) {
        if (subsystem in subsystems) {
            subsystems.remove(subsystem)
        }

        if (subsystem is Loopable && subsystem in loopableSubsystems) {
            loopableSubsystems.remove(subsystem)
        }

        if (subsystem is Zeroable && subsystem in zeroableSubsystems) {
            zeroableSubsystems.remove(subsystem)
        }

        if (subsystem is Readable && subsystem in readableSubsystems) {
            readableSubsystems.remove(subsystem)
        }

        if (subsystem is Testable && subsystem in testableSubsystems) {
            testableSubsystems.remove(subsystem)
        }

        if (subsystem is Initializable && subsystem in initializableSubsystems) {
            initializableSubsystems.remove(subsystem)
        }

        if (subsystem is Startable && subsystem in startableSubsystems) {
            startableSubsystems.remove(subsystem)
        }
    }

    fun clearAll() {
        managerLists.forEach(ArrayList<*>::clear)
    }

    override fun toString(): String {
        var ret = ""

        subsystems.forEach { ret += "${it::class.java} \n" }

        return ret
    }
}
