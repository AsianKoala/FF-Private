package robotuprising.ftc2021.util.manager

import robotuprising.ftc2021.subsystems.wraith.Subsystem
import robotuprising.ftc2021.util.Loopable
import robotuprising.ftc2021.util.Readable
import robotuprising.ftc2021.util.Testable
import robotuprising.ftc2021.util.Zeroable

object SubsystemManager {
    private val subsystems: ArrayList<Subsystem> = ArrayList()
    private val loopableSubsystems: ArrayList<Loopable> = ArrayList()
    private val zeroableSubsystems: ArrayList<Zeroable> = ArrayList()
    private val readableSubsystems: ArrayList<Readable> = ArrayList()
    private val testableSubsystems: ArrayList<Testable> = ArrayList()

    fun resetAll() {
        subsystems.forEach(Subsystem::reset)
    }

    fun updateDashboardAll() {
        subsystems.forEach(Subsystem::updateDashboard)
    }

    fun loopAll() {
        loopableSubsystems.forEach(Loopable::loop)
    }

    fun zeroAll() {
        zeroableSubsystems.forEach(Zeroable::zero)
    }

    fun readAll() {
        readableSubsystems.forEach(Readable::read)
    }

    fun testAll() {
        testableSubsystems.forEach(Testable::test)
    }

    fun setSubsystems(vararg allSubsystems: Subsystem) {
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

        if(subsystem is Readable && subsystem in readableSubsystems) {
            readableSubsystems.remove(subsystem)
        }
    }

    override fun toString(): String {
        var ret = ""

        subsystems.forEach { ret += it::class.java }

        return ret
    }
}