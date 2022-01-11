package robotuprising.ftc2021.manager

import robotuprising.ftc2021.subsystems.osiris.Subsystem
import robotuprising.ftc2021.hardware.osiris.interfaces.Loopable
import robotuprising.ftc2021.hardware.osiris.interfaces.Readable
import robotuprising.ftc2021.hardware.osiris.interfaces.Testable
import robotuprising.ftc2021.hardware.osiris.interfaces.Zeroable

object SubsystemManager {
    private val subsystems: ArrayList<Subsystem> = ArrayList()
    private val loopableSubsystems: ArrayList<Loopable> = ArrayList()
    private val zeroableSubsystems: ArrayList<Zeroable> = ArrayList()
    private val readableSubsystems: ArrayList<Readable> = ArrayList()
    private val testableSubsystems: ArrayList<Testable> = ArrayList()

    private var zeroedYet = false

    fun resetAll() {
        subsystems.forEach(Subsystem::reset)
    }

    fun updateDashboardAll(debugging: Boolean) {
        subsystems.forEach { it.updateDashboard(debugging) }
    }

    fun loopAll() {
        loopableSubsystems.forEach(Loopable::loop)
    }

    fun zeroAll() {
        zeroableSubsystems.forEach(Zeroable::zero)

        zeroedYet = true
    }

    fun readAll() {
        readableSubsystems.forEach(Readable::read)
    }

    fun testAll() {
        testableSubsystems.forEach(Testable::test)
    }

    fun prepareForInit() {

    }

    fun init() {
        resetAll()
    }

    fun registerSubsystems(vararg allSubsystems: Subsystem) {
        allSubsystems.forEach { register(it) }
    }

    fun deregisterSubsystems(vararg allSubsystems: Subsystem) {
        allSubsystems.forEach { deregister(it) }
    }

    fun register(subsystem: Subsystem) {
        subsystems.add(subsystem)

        if(subsystem is Loopable) {
            loopableSubsystems.add(subsystem)
        }

        if(subsystem is Zeroable) {
            zeroableSubsystems.add(subsystem)
        }

        if(subsystem is Readable) {
            readableSubsystems.add(subsystem)
        }

        if(subsystem is Testable) {
            testableSubsystems.add(subsystem)
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
    }

    override fun toString(): String {
        var ret = ""

        subsystems.forEach { ret += it::class.java }

        return ret
    }
}