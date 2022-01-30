package robotuprising.ftc2021.manager

import robotuprising.ftc2021.hardware.osiris.interfaces.*
import robotuprising.ftc2021.subsystems.osiris.Subsystem
import robotuprising.lib.opmode.OsirisDashboard

object SubsystemManager {
    private val subsystems: ArrayList<Subsystem> = ArrayList()
    private val loopableSubsystems: ArrayList<Loopable> = ArrayList()
    private val zeroableSubsystems: ArrayList<Zeroable> = ArrayList()
    private val readableSubsystems: ArrayList<Readable> = ArrayList()
    private val testableSubsystems: ArrayList<Testable> = ArrayList()
    private val initializableSubsystems: ArrayList<Initializable> = ArrayList()

    private val managerLists = listOf(subsystems, loopableSubsystems, zeroableSubsystems,
            readableSubsystems, testableSubsystems, initializableSubsystems)

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
    }

    fun deregister(subsystem: Subsystem) {
        if(subsystem in subsystems) {
            subsystems.remove(subsystem)
        }

        if(subsystem is Loopable && subsystem in loopableSubsystems) {
            loopableSubsystems.remove(subsystem)
        }

        if(subsystem is Zeroable && subsystem in zeroableSubsystems) {
            zeroableSubsystems.remove(subsystem)
        }

        if(subsystem is Readable && subsystem in readableSubsystems) {
            readableSubsystems.remove(subsystem)
        }

        if(subsystem is Testable && subsystem in testableSubsystems) {
            testableSubsystems.remove(subsystem)
        }

        if(subsystem is Initializable && subsystem in initializableSubsystems) {
            initializableSubsystems.remove(subsystem)
        }
    }

    fun clearAll() {
        managerLists.forEach { it.clear() }
    }

    override fun toString(): String {
        var ret = ""

        subsystems.forEach { ret += "${it::class.java} \n"}

        return ret
    }
}