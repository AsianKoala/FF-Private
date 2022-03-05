package asiankoala.junk.v2.opmodes

import asiankoala.junk.v2.lib.system.BaseOpMode
import asiankoala.junk.v2.manager.*
import asiankoala.junk.v2.subsystems.IntakeStopper
import asiankoala.junk.v2.subsystems.hardware.*

abstract class OsirisOpMode : BaseOpMode() {

    open fun register() {
        SubsystemManager.registerSubsystems(
            Ghost,
//
            Intake,
            LoadingSensor,
//
            Outtake,
            Indexer,
            Arm,
//
            Turret,
            Slides,
//
            Spinner,
//
            IntakeStopper,

//                BlueWebcam,
//                RedWebcam
        )
    }

    override fun mInit() {
        SubsystemManager.clearAll()
        register()
        SubsystemManager.initAll()

        Turret.zero()
        Slides.setSlideInches(0.0)
    }

    override fun mInitLoop() {
        SubsystemManager.periodic()
        SubsystemManager.initServos()
        StateMachineManager.periodic()
    }

    override fun mStart() {
//        SubsystemManager.deregister(BlueWebcam)
        SubsystemManager.startAll()
        IntakeStopper.unlock()
    }

    override fun mLoop() {
        SubsystemManager.periodic()
        StateMachineManager.periodic()
    }

    override fun mStop() {
        SubsystemManager.stopAll()
        StateMachineManager.stop()
    }
}
